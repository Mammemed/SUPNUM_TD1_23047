package com.supnum.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Configuration du WebClient pour appeler le middle-service.
 */
@Configuration
public class WebClientConfig {

	@Value("${middle.service.url}")
	private String middleServiceUrl;

	/**
	 * Configure le WebClient pour appeler le middle-service.
	 * 
	 * @return WebClient configuré
	 */
	@Bean
	public WebClient webClient() {
		return WebClient.builder()
				.baseUrl(middleServiceUrl)
				.build();
	}
}

