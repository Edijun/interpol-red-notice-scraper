package id.edijun.interpolrednoticescraper.repository.payload;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticePayload {

	@JsonProperty("arrest_warrants")
	private List<ArrestWarrantPayload> arrestWarrants;

	@JsonProperty("country_of_birth_id")
	private String countryOfBirthId;

	@JsonProperty("date_of_birth")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy/MM/dd")
	private Date dateOfBirth;

	@JsonProperty("distinguishing_marks")
	private String distinguishingMarks;

	@JsonProperty("entity_id")
	private String entityId;

	@JsonProperty("eyes_colors_id")
	private List<String> eyesColorsId;

	@JsonProperty("forename")
	private String forename;

	@JsonProperty("hairs_id")
	private List<String> hairsId;

	@JsonProperty("height")
	private Float height;

	@JsonProperty("languages_spoken_ids")
	private List<String> languagesSpokenIds;

	@JsonProperty("name")
	private String name;

	@JsonProperty("nationalities")
	private List<String> nationalities;

	@JsonProperty("place_of_birth")
	private String placeOfBirth;

	@JsonProperty("sex_id")
	private String sexId;

	@JsonProperty("weight")
	private Integer weight;

	@JsonProperty("_embedded")
	private EmbeddedPayload embedded;

	@JsonProperty("_links")
	private LinksPayload links;

	@JsonProperty("images")
	private List<ImagePayload> images;

}
