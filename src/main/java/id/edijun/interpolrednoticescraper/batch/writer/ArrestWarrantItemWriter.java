package id.edijun.interpolrednoticescraper.batch.writer;

import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import id.edijun.interpolrednoticescraper.model.ArrestWarrant;
import id.edijun.interpolrednoticescraper.model.Notice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ArrestWarrantItemWriter implements ItemWriter<Notice> {

	private JdbcTemplate jdbcTemplate;
	
	private final String sql = "INSERT INTO arrest_warrant ("
			+ "charge, "
			+ "charge_translation, "
			+ "issuing_country_id, "
			+ "entity_id"
			+ ") VALUES (?, ?, ?, ?)";

	public ArrestWarrantItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends Notice> items) throws Exception {
		for (Notice notice : items) {
			if (notice.getLink().getStatus().equals("EXIST")) {
				if (Objects.nonNull(notice.getArrestWarrants())) {
					for(ArrestWarrant arrestWarrant : notice.getArrestWarrants()) {
						jdbcTemplate.update(sql, 
								arrestWarrant.getCharge(),
								arrestWarrant.getChargeTranslation(),
								arrestWarrant.getIssuingCountryId(),
								notice.getEntityId());
					}
				} else {
					log.info("Notice {} has no arrest_warrant!", notice.getEntityId());
				}
			}
		}

	}

}
