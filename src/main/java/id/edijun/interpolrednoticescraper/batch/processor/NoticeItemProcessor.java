package id.edijun.interpolrednoticescraper.batch.processor;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import id.edijun.interpolrednoticescraper.mapper.RowMapper;
import id.edijun.interpolrednoticescraper.model.ArrestWarrant;
import id.edijun.interpolrednoticescraper.model.Image;
import id.edijun.interpolrednoticescraper.model.Link;
import id.edijun.interpolrednoticescraper.model.Notice;
import id.edijun.interpolrednoticescraper.repository.NoticeApiRepository;
import id.edijun.interpolrednoticescraper.repository.payload.ArrestWarrantPayload;
import id.edijun.interpolrednoticescraper.repository.payload.EmbeddedPayload;
import id.edijun.interpolrednoticescraper.repository.payload.ImagePayload;
import id.edijun.interpolrednoticescraper.repository.payload.NoticePayload;
import id.edijun.interpolrednoticescraper.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NoticeItemProcessor implements ItemProcessor<Link, Notice> {
	
	/*
	 * - Check if year-id available on database
	 * - If year-id exist then skip the process
	 * - If not then ping test year-id
	 * - If ping test success, download the notice data
	 * */
	
	@Autowired
	private NoticeApiRepository noticeApiRepository;
	
	private JdbcTemplate jdbcTemplate;
	
	public NoticeItemProcessor(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Notice process(Link item) throws Exception {
		
		log.info("Processing id {} ...", item.getEntityId());
		
		Notice notice = new Notice();
		
		if (!isYearIdExist(item.getEntityId())) {
			if(isPingSuccess(item.getEntityId())) {
				notice = downloadData(item.getEntityId());
				item.setStatus("EXIST");
			} else {
				item.setStatus("NOTEXIST");
			}
		} else {
			item.setStatus("DONE");
		}
		
		notice.setLink(item);
		return notice;
	}
	
	private boolean isYearIdExist(String entityId) {
		String query = "SELECT COUNT(entity_id) FROM notice WHERE entity_id = ?";
		Integer idExist = jdbcTemplate.queryForObject(query, Integer.class, entityId);
		if (idExist > 0) {
			return true;
		}
		return false;
	}
	
	private boolean isPingSuccess(String entityId) {
		String uri = CommonUtil.entityIdToLink(entityId);
		try {
			URL url = new URL(uri);
			HttpURLConnection huc = (HttpURLConnection) url.openConnection();

			if (huc.getResponseCode() == 200) {
				return true;
			}

		} catch (Exception e) {
			log.error("Error ping : {} with error {}", entityId, e.getMessage());
		}

		return false;
	}
	
	private Notice downloadData(String entityId) {
		log.info("Start download data with ID : {}", entityId);

		String uri = CommonUtil.entityIdToLink(entityId);
		Notice notice = null;
		try {
			log.info("Get notice data from : {} ", uri);
			NoticePayload noticePayload = noticeApiRepository.getNotice(uri);
			notice = RowMapper.mapNotice(noticePayload);

			// map arrest warrant
			List<ArrestWarrant> listArrestWarrant = new ArrayList<ArrestWarrant>();
			for (ArrestWarrantPayload ArrestWarrantPayload : noticePayload.getArrestWarrants()) {
				ArrestWarrant ArrestWarrant = RowMapper.mapArrestWarrant(ArrestWarrantPayload);
				listArrestWarrant.add(ArrestWarrant);
			}

			log.info("Download image links from : {} ", noticePayload.getLinks().getImages().getHref());
			EmbeddedPayload embeddedPayload = noticeApiRepository
					.getImagesUrl(noticePayload.getLinks().getImages().getHref());

			// map images
			List<Image> listImage = new ArrayList<Image>();
			for (ImagePayload imagePayload : embeddedPayload.getEmbedded().getImages()) {
				String imageUrl = imagePayload.getLinks().getSelf().getHref();

				log.info("Download image from : {} ", imageUrl);
				String imageBase64 = noticeApiRepository.getImageAsBase64(imagePayload.getPictureId(), imageUrl);

				Image newImage = new Image();
				newImage.setBase64(imageBase64);
				newImage.setPictureId(imagePayload.getPictureId());
				listImage.add(newImage);
			}
			notice.setImages(listImage);

		} catch (Exception e) {
			log.error("ERROR PROCESSING NOTICE WITH ID : {}", entityId);
		}

		return notice;
	}

}
