package id.edijun.interpolrednoticescraper;

import org.apache.logging.log4j.core.config.Configurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class InterpolRedNoticeScraperApplication implements CommandLineRunner {

	@Value("${app.log4j.path}")
	private String log4path;

	public static void main(String[] args) {
		SpringApplication.run(InterpolRedNoticeScraperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Configurator.initialize(null, log4path);
		log.info("Initialize logger application...");
	}

}
