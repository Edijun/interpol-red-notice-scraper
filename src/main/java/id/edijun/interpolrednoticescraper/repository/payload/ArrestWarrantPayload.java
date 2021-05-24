package id.edijun.interpolrednoticescraper.repository.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArrestWarrantPayload {

	@JsonProperty("charge")
	private String charge;

	@JsonProperty("charge_translation")
	private String chargeTranslation;

	@JsonProperty("issuing_country_id")
	private String issuingCountryId;

}
