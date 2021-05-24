package id.edijun.interpolrednoticescraper.batch.writer;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.JdbcTemplate;

import id.edijun.interpolrednoticescraper.model.Notice;


public class NoticeItemWriter implements ItemWriter<Notice> {

	private JdbcTemplate jdbcTemplate;
	
	private final String sql = "INSERT INTO notice ("
			+ "entity_id, "
			+ "country_of_birth_id, "
			+ "date_of_birth, "
			+ "distinguishing_marks, "
			+ "eyes_colors_id, "
			+ "forename, "
			+ "hairs_id, "
			+ "height, "
			+ "languages_spoken_ids, "
			+ "name, "
			+ "nationalities, "
			+ "place_of_birth, "
			+ "sex_id, "
			+ "weight, "
			+ "updated_at"
			+ ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	public NoticeItemWriter(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void write(List<? extends Notice> items) throws Exception {
		for (Notice notice : items) {
			
			if (notice.getLink().getStatus().equals("EXIST")) {
				jdbcTemplate.update(sql, 
					notice.getEntityId(),
					notice.getCountryOfBirthId(),
					Objects.nonNull(notice.getDateOfBirth()) ? notice.getDateOfBirth() : null, 
					notice.getDistinguishingMarks(),
					notice.getEyesColorsId(),
					notice.getForename(),
					notice.getHairsId(),
					Objects.nonNull(notice.getHeight()) ? notice.getHeight() : null, 
					notice.getLanguagesSpokenIds(),
					notice.getName(),
					notice.getNationalities(),
					notice.getPlaceOfBirth(),
					notice.getSexId(),
					Objects.nonNull(notice.getWeight()) ? notice.getWeight() : null,
					new Date());
			}
		}

	}

}
