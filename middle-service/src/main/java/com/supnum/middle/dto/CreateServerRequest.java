package com.supnum.middle.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateServerRequest {
	
	@NotBlank(message = "Le nom du serveur est obligatoire")
	private String name;
	
	@NotBlank(message = "L'adresse IP est obligatoire")
	@Pattern(regexp = "^([0-9]{1,3}\\.){3}[0-9]{1,3}$", message = "L'adresse IP doit être au format valide (ex: 192.168.1.1)")
	private String ipAddress;
}

