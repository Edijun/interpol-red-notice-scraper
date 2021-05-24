package id.edijun.interpolrednoticescraper.repository.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagePayload {

	@JsonProperty("picture_id")
	private String pictureId;

	@JsonProperty("_links")
	private LinksPayload links;

	@JsonProperty("base64")
	private String base64;

}
