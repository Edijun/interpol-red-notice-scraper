package id.edijun.interpolrednoticescraper.repository;

import id.edijun.interpolrednoticescraper.repository.payload.EmbeddedPayload;
import id.edijun.interpolrednoticescraper.repository.payload.NoticePayload;

public interface NoticeApiRepository {

	NoticePayload getNotice(String url);

	EmbeddedPayload getImagesUrl(String url);

	String getImageAsBase64(String imageId, String url);

}
