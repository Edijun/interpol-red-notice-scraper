package id.edijun.interpolrednoticescraper.repository.payload;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmbeddedPayload {
	
	@JsonProperty("_links")
	private LinksPayload links;
	
	@JsonProperty("images")
	private List<ImagePayload> images;
	
	@JsonProperty("_embedded")
	private EmbeddedPayload embedded;

}
