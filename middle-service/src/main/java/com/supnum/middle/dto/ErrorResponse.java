package com.supnum.middle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour les réponses d'erreur de l'API.
 * 
 * Cette classe représente une réponse d'erreur standardisée avec toutes les informations nécessaires.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
	
	/**
	 * Timestamp de l'erreur.
	 */
	private LocalDateTime timestamp;
	
	/**
	 * Code de statut HTTP.
	 */
	private int status;
	
	/**
	 * Type d'erreur (ex: "Bad Request", "Internal Server Error").
	 */
	private String error;
	
	/**
	 * Message d'erreur détaillé.
	 */
	private String message;
	
	/**
	 * Chemin de la requête qui a causé l'erreur.
	 */
	private String path;
}

