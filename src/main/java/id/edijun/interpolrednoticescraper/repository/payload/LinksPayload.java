package id.edijun.interpolrednoticescraper.repository.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LinksPayload {

	@JsonProperty("self")
	private LinkPayload self;

	@JsonProperty("first")
	private LinkPayload first;

	@JsonProperty("next")
	private LinkPayload next;

	@JsonProperty("last")
	private LinkPayload last;

	@JsonProperty("images")
	private LinkPayload images;

	@JsonProperty("thumbnail")
	private LinkPayload thumbnail;
}
