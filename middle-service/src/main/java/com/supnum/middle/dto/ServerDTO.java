package com.supnum.middle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant un serveur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerDTO {
	
	private Long id;
	
	private String name;
	
	private String ipAddress;
	
	private Boolean status;
}

