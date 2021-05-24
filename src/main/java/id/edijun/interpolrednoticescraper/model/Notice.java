package id.edijun.interpolrednoticescraper.model;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Notice {
	
	private String entityId;
	
	private String countryOfBirthId;
	
	private Date dateOfBirth;
	
	private String distinguishingMarks;
	
	private String forename;
	
	private String hairsId;
	
	private Float height;
	
	private String languagesSpokenIds;
	
	private String name;
	
	private String nationalities;
	
	private String placeOfBirth;
	
	private String sexId;
	
	private Integer weight;
	
	private String eyesColorsId;

	private List<ArrestWarrant> arrestWarrants;

	private Link link;

	private List<Image> images;

}
