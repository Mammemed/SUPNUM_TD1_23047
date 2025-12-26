package com.supnum.consumer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la requête de renommage d'un serveur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RenameServerRequestDTO {
	
	@NotBlank(message = "Le nouveau nom du serveur est obligatoire")
	private String newName;
}

