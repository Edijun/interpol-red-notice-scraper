package id.edijun.interpolrednoticescraper.mapper;

import id.edijun.interpolrednoticescraper.model.ArrestWarrant;
import id.edijun.interpolrednoticescraper.model.Notice;
import id.edijun.interpolrednoticescraper.repository.payload.ArrestWarrantPayload;
import id.edijun.interpolrednoticescraper.repository.payload.NoticePayload;
import id.edijun.interpolrednoticescraper.util.CommonUtil;

public class RowMapper {

	public static Notice mapNotice(NoticePayload payload) {
		Notice notice = new Notice();

		notice.setEntityId(payload.getEntityId());
		notice.setCountryOfBirthId(payload.getCountryOfBirthId());
		notice.setDateOfBirth(payload.getDateOfBirth());
		notice.setDistinguishingMarks(payload.getDistinguishingMarks());
		notice.setForename(payload.getForename());
		notice.setHairsId(CommonUtil.listStringToCommaSeparated(payload.getHairsId()));
		notice.setHeight(payload.getHeight());
		notice.setLanguagesSpokenIds(CommonUtil.listStringToCommaSeparated(payload.getLanguagesSpokenIds()));
		notice.setName(payload.getName());
		notice.setNationalities(CommonUtil.listStringToCommaSeparated(payload.getNationalities()));
		notice.setPlaceOfBirth(payload.getPlaceOfBirth());
		notice.setSexId(payload.getSexId());
		notice.setWeight(payload.getWeight());
		notice.setEyesColorsId(CommonUtil.listStringToCommaSeparated(payload.getEyesColorsId()));

		return notice;

	}

	public static ArrestWarrant mapArrestWarrant(ArrestWarrantPayload payload) {
		ArrestWarrant arrestWarrant = new ArrestWarrant();

		arrestWarrant.setCharge(payload.getCharge());
		arrestWarrant.setChargeTranslation(payload.getChargeTranslation());
		arrestWarrant.setIssuingCountryId(payload.getIssuingCountryId());

		return arrestWarrant;
	}

}
