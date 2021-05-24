package id.edijun.interpolrednoticescraper.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;

@Configuration
public class WebFluxConfig implements WebFluxConfigurer {

	@Bean
	public WebClient getWebClient() {
		HttpClient httpClient = HttpClient.create();

		ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));

		return WebClient.builder().clientConnector(connector)
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.build();
	}
}
