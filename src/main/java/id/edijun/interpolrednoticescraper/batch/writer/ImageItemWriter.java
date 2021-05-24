package id.edijun.interpolrednoticescraper.batch.writer;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import id.edijun.interpolrednoticescraper.model.Image;
import id.edijun.interpolrednoticescraper.model.Notice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImageItemWriter implements ItemWriter<Notice> {

	private JdbcTemplate jdbcTemplate;
	
	private final String sql = "INSERT INTO image (picture_id, base64, entity_id) VALUES (?, ?, ?)";

	public ImageItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends Notice> items) throws Exception {
		for (Notice notice : items) {
			if (notice.getLink().getStatus().equals("EXIST")) {
				if (Objects.nonNull(notice.getImages())) {
					for (Image image : notice.getImages()) {
						jdbcTemplate.update(sql, 
								image.getPictureId(),
								image.getBase64(),
								notice.getEntityId());
					}
				} else {
					log.info("Notice {} has no image!", notice.getEntityId());
				}
			}
		}

	}

}
