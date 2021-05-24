package id.edijun.interpolrednoticescraper.batch.reader;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import id.edijun.interpolrednoticescraper.model.Link;
import id.edijun.interpolrednoticescraper.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EntityItemReader implements ItemReader<Link> {
	
	/*
	 * Generate all possible 400000 year-id from 1950 to 2021
	 * */
	
	private JdbcTemplate jdbcTemplate;

	private List<Link> linkData;
	private int nextLinkIndex;

	public EntityItemReader(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		initialize();
	}

	private void initialize() {

		log.info("Initialize reader...");

		int startYear = 1950;
		int endYear = 2021;
		int totalTestId = 400000;

		String lastEntityId = findlastEntityId();
		startYear = CommonUtil.getYearFromEntityId(lastEntityId);

		log.info("Last yearId = {}", lastEntityId);

		linkData = new ArrayList<Link>();
		for (int year = startYear; year <= endYear; year++) {
			int startId = 0;
			if (year == startYear) {
				startId = CommonUtil.getIdFromEntityId(lastEntityId) + 1;
			}

			for (int id = startId; id < totalTestId; id++) {
				String entityId = year + "/" + id;

				Link link = new Link();
				link.setEntityId(entityId);
				link.setYear(year);
				link.setSubId(id);

				linkData.add(link);
			}
		}

		nextLinkIndex = 0;
	}

	@Override
	public Link read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		Link nextLink = null;
		if (nextLinkIndex < linkData.size()) {
			nextLink = linkData.get(nextLinkIndex);
			nextLinkIndex++;
		} else {
			nextLinkIndex = 0;
		}

		return nextLink;
	}
	
	private String findlastEntityId() {
		
		log.info("Find last entityId...");
		
		String sqlLastYear = "SELECT entity_id FROM link ORDER BY entity_id DESC LIMIT 1;";
		String lastYearId = jdbcTemplate.queryForObject(sqlLastYear, String.class);

		Integer lastYear = CommonUtil.getYearFromEntityId(lastYearId);
		
		String sqlLastId = "SELECT sub_id FROM link WHERE year = " + lastYear + ";";
		List<Integer> lastYearIds = jdbcTemplate.query(sqlLastId, new RowMapper<Integer>() {
			@Override
			public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getInt(1);
			}
		});

		Collections.sort(lastYearIds);

		Integer lastId = lastYearIds.get(lastYearIds.size() - 1);

		return lastYear + "/" + lastId;
	}

}
