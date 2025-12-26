package com.supnum.middle.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration OpenAPI/Swagger pour la documentation de l'API REST.
 * 
 * Cette classe configure la documentation Swagger/OpenAPI pour le middle-service.
 */
@Configuration
public class OpenApiConfig {

	/**
	 * Configure l'API OpenAPI avec les informations de base, le contact et le serveur.
	 * 
	 * @return OpenAPI configuré
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Middle Service - Server Management API")
						.description("REST API pour gérer les serveurs via le backend SOAP")
						.version("1.0.0")
						.contact(new Contact()
								.name("SUPNUM Team")
								.email("support@supnum.com")))
				.servers(List.of(
						new Server()
								.url("http://localhost:8081")
								.description("Serveur de développement local")
				));
	}
}
