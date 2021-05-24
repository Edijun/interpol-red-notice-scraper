package id.edijun.interpolrednoticescraper.util;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;

public class CommonUtil {
	
	@Value("${base.notice.url}")
    private static String baseNoticeUrl;
	
	public static String entityIdToLink(String entityId) {
		return baseNoticeUrl + entityId.replace("/", "-");
	}
	
	public static String listStringToCommaSeparated(List<String> source) {

		if (Objects.nonNull(source)) {
			return String.join(",", source);
		}

		else {
			return null;
		}

	}
	
	public static Integer getYearFromEntityId(String entityId) {
		return Integer.parseInt(entityId.substring(0, 4));
	}
	
	public static Integer getIdFromEntityId(String entityId) {
		return Integer.parseInt(entityId.substring(5));
	}
}
