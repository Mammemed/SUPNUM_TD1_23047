package com.supnum.middle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la réponse contenant les informations d'un serveur avec un message.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponseDTO {
	
	private Long id;
	
	private String name;
	
	private String ipAddress;
	
	private Boolean status;
	
	private String message;
}

