package com.supnum.consumer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête de création d'un serveur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateServerRequestDTO {
	
	@NotBlank(message = "Le nom du serveur est obligatoire")
	private String name;
	
	@NotBlank(message = "L'adresse IP est obligatoire")
	@Pattern(regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "L'adresse IP doit être au format valide (ex: 192.168.1.1)")
	private String ipAddress;
}

