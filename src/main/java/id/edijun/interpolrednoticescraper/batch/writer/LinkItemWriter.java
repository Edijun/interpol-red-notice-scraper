package id.edijun.interpolrednoticescraper.batch.writer;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import id.edijun.interpolrednoticescraper.model.Notice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LinkItemWriter implements ItemWriter<Notice> {

	private JdbcTemplate jdbcTemplate;
	
	private final String sql = "INSERT INTO link ("
			+ "entity_id, "
			+ "status, "
			+ "year, "
			+ "sub_id"
			+ ") VALUES (?, ?, ?, ?)";

	public LinkItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends Notice> items) throws Exception {
		for (Notice notice : items) {
			if (Objects.nonNull(notice.getLink())) {
				if (notice.getLink().getStatus().equals("EXIST")) {
					notice.getLink().setStatus("DONE");
				}
				
//				log.info(notice.getLink().toString());
				
				jdbcTemplate.update(sql, 
						notice.getLink().getEntityId(),
						notice.getLink().getStatus(),
						notice.getLink().getYear(),
						notice.getLink().getSubId());
				
			} else {
				log.info("Notice {} has no link!", notice.getEntityId());
			}
		}

	}

}
