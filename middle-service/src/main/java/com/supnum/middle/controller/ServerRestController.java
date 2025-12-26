package com.supnum.middle.controller;

import com.supnum.middle.dto.CreateServerRequestDTO;
import com.supnum.middle.dto.RenameServerRequestDTO;
import com.supnum.middle.dto.ServerResponseDTO;
import com.supnum.middle.service.ServerRestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST pour la gestion des serveurs.
 * 
 * Ce controller expose des endpoints REST qui consomment le service SOAP via ServerRestService.
 */
@Slf4j
@RestController
@RequestMapping("/api/servers")
@RequiredArgsConstructor
@Tag(name = "Server Management", description = "API REST pour la gestion des serveurs")
public class ServerRestController {

	private final ServerRestService serverRestService;

	/**
	 * Crée un nouveau serveur.
	 * 
	 * @param request DTO contenant les informations du serveur à créer
	 * @return ResponseEntity avec le serveur créé (201 Created)
	 */
	@PostMapping
	@Operation(
			summary = "Créer un nouveau serveur",
			description = "Crée un nouveau serveur avec un nom et une adresse IP"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Serveur créé avec succès"),
			@ApiResponse(responseCode = "400", description = "Requête invalide (validation échouée)"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<ServerResponseDTO> createServer(@Valid @RequestBody CreateServerRequestDTO request) {
		log.info("Requête POST /api/servers - Création d'un serveur: name={}, ipAddress={}", 
				request.getName(), request.getIpAddress());
		
		ServerResponseDTO response = serverRestService.createServer(request);
		
		log.info("Serveur créé avec succès: id={}", response.getId());
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	/**
	 * Liste tous les serveurs.
	 * 
	 * @return ResponseEntity avec la liste des serveurs (200 OK)
	 */
	@GetMapping
	@Operation(
			summary = "Lister tous les serveurs",
			description = "Récupère la liste de tous les serveurs disponibles"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Liste des serveurs récupérée avec succès"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<List<ServerResponseDTO>> getAllServers() {
		log.info("Requête GET /api/servers - Récupération de tous les serveurs");
		
		List<ServerResponseDTO> servers = serverRestService.getAllServers();
		
		log.info("Nombre de serveurs récupérés: {}", servers.size());
		return ResponseEntity.ok(servers);
	}

	/**
	 * Renomme un serveur.
	 * 
	 * @param id L'ID du serveur à renommer
	 * @param request DTO contenant le nouveau nom
	 * @return ResponseEntity avec le serveur renommé (200 OK)
	 */
	@PutMapping("/{id}/rename")
	@Operation(
			summary = "Renommer un serveur",
			description = "Renomme un serveur existant avec un nouveau nom"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Serveur renommé avec succès"),
			@ApiResponse(responseCode = "400", description = "Requête invalide (validation échouée)"),
			@ApiResponse(responseCode = "404", description = "Serveur non trouvé"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<ServerResponseDTO> renameServer(
			@PathVariable Long id,
			@Valid @RequestBody RenameServerRequestDTO request) {
		log.info("Requête PUT /api/servers/{}/rename - Renommage du serveur: newName={}", id, request.getNewName());
		
		ServerResponseDTO response = serverRestService.renameServer(id, request);
		
		log.info("Serveur renommé avec succès: id={}, newName={}", id, request.getNewName());
		return ResponseEntity.ok(response);
	}

	/**
	 * Récupère le statut d'un serveur.
	 * 
	 * @param id L'ID du serveur
	 * @return ResponseEntity avec le statut du serveur (200 OK)
	 */
	@GetMapping("/{id}/status")
	@Operation(
			summary = "Obtenir le statut d'un serveur",
			description = "Récupère le statut (en cours d'exécution ou arrêté) d'un serveur"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Statut du serveur récupéré avec succès"),
			@ApiResponse(responseCode = "404", description = "Serveur non trouvé"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<ServerResponseDTO> getServerStatus(@PathVariable Long id) {
		log.info("Requête GET /api/servers/{}/status - Récupération du statut du serveur", id);
		
		ServerResponseDTO response = serverRestService.getServerStatus(id);
		
		log.info("Statut du serveur récupéré: id={}, status={}", id, response.getStatus());
		return ResponseEntity.ok(response);
	}

	/**
	 * Démarre un serveur.
	 * 
	 * @param id L'ID du serveur à démarrer
	 * @return ResponseEntity avec le serveur démarré (200 OK)
	 */
	@PutMapping("/{id}/start")
	@Operation(
			summary = "Démarrer un serveur",
			description = "Démarre un serveur arrêté"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Serveur démarré avec succès"),
			@ApiResponse(responseCode = "404", description = "Serveur non trouvé"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<ServerResponseDTO> startServer(@PathVariable Long id) {
		log.info("Requête PUT /api/servers/{}/start - Démarrage du serveur", id);
		
		ServerResponseDTO response = serverRestService.startServer(id);
		
		log.info("Serveur démarré avec succès: id={}", id);
		return ResponseEntity.ok(response);
	}

	/**
	 * Arrête un serveur.
	 * 
	 * @param id L'ID du serveur à arrêter
	 * @return ResponseEntity avec le serveur arrêté (200 OK)
	 */
	@PutMapping("/{id}/stop")
	@Operation(
			summary = "Arrêter un serveur",
			description = "Arrête un serveur en cours d'exécution"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Serveur arrêté avec succès"),
			@ApiResponse(responseCode = "404", description = "Serveur non trouvé"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<ServerResponseDTO> stopServer(@PathVariable Long id) {
		log.info("Requête PUT /api/servers/{}/stop - Arrêt du serveur", id);
		
		ServerResponseDTO response = serverRestService.stopServer(id);
		
		log.info("Serveur arrêté avec succès: id={}", id);
		return ResponseEntity.ok(response);
	}

	/**
	 * Supprime un serveur.
	 * 
	 * @param id L'ID du serveur à supprimer
	 * @return ResponseEntity vide (204 No Content)
	 */
	@DeleteMapping("/{id}")
	@Operation(
			summary = "Supprimer un serveur",
			description = "Supprime un serveur. Le serveur doit être arrêté pour pouvoir être supprimé"
	)
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Serveur supprimé avec succès"),
			@ApiResponse(responseCode = "404", description = "Serveur non trouvé"),
			@ApiResponse(responseCode = "400", description = "Le serveur est en cours d'exécution et ne peut pas être supprimé"),
			@ApiResponse(responseCode = "500", description = "Erreur serveur")
	})
	public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
		log.info("Requête DELETE /api/servers/{} - Suppression du serveur", id);
		
		serverRestService.deleteServer(id);
		
		log.info("Serveur supprimé avec succès: id={}", id);
		return ResponseEntity.noContent().build();
	}
}
