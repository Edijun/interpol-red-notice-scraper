package id.edijun.interpolrednoticescraper.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import id.edijun.interpolrednoticescraper.repository.payload.EmbeddedPayload;
import id.edijun.interpolrednoticescraper.repository.payload.NoticePayload;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.Base64;

@Slf4j
@Repository
public class NoticeApiRepositoryImpl implements NoticeApiRepository {

	@Autowired
	private WebClient webClient;

	@Override
	public NoticePayload getNotice(String url) {
		return webClient.get()
				.uri(url)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("4xx"));
				})
				.onStatus(HttpStatus::is5xxServerError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("5xx"));
				})
				.bodyToMono(NoticePayload.class).block();
	}

	@Override
	public EmbeddedPayload getImagesUrl(String url) {
		return webClient.get()
				.uri(url)
				.retrieve()
				.onStatus(HttpStatus::is4xxClientError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("4xx"));
				})
				.onStatus(HttpStatus::is5xxServerError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("5xx"));
				})
				.bodyToMono(EmbeddedPayload.class).block();
	}

	@Override
	public String getImageAsBase64(String imageId, String url) {
		
		byte[] image = WebClient.create(url)
		        .get()
		        .accept(MediaType.IMAGE_JPEG)
		        .retrieve()
		        .onStatus(HttpStatus::is4xxClientError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("4xx"));
				})
				.onStatus(HttpStatus::is5xxServerError, response -> {
					log.error("Error {} on get {}", response.rawStatusCode(), url);
					return Mono.error(new RuntimeException("5xx"));
				})
		        .bodyToMono(byte[].class)
		        .block();

		String encodedString = Base64.getEncoder().encodeToString(image);

		return encodedString;
	}

}
