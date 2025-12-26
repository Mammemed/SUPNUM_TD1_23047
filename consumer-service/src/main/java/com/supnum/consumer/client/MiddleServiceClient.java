package com.supnum.consumer.client;

import com.supnum.consumer.dto.CreateServerRequestDTO;
import com.supnum.consumer.dto.RenameServerRequestDTO;
import com.supnum.consumer.dto.ServerResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

/**
 * Client REST pour appeler le middle-service.
 * 
 * Cette classe encapsule tous les appels HTTP vers le middle-service REST API.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MiddleServiceClient {

	private final WebClient webClient;

	/**
	 * Crée un nouveau serveur via le middle-service.
	 * 
	 * @param request DTO contenant les informations du serveur à créer
	 * @return ServerResponseDTO contenant le serveur créé
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public ServerResponseDTO createServer(CreateServerRequestDTO request) {
		log.info("Appel REST pour créer un serveur: name={}, ipAddress={}", 
				request.getName(), request.getIpAddress());
		
		try {
			ServerResponseDTO response = webClient.post()
					.uri("/api/servers")
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request)
					.retrieve()
					.bodyToMono(ServerResponseDTO.class)
					.block();
			
			log.debug("Réponse REST reçue pour createServer: id={}, name={}", 
					response != null ? response.getId() : null,
					response != null ? response.getName() : null);
			
			return response;
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors de la création du serveur: status={}, message={}", 
					e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour créer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la création du serveur", e);
			throw new RuntimeException("Erreur inattendue lors de la création du serveur", e);
		}
	}

	/**
	 * Liste tous les serveurs via le middle-service.
	 * 
	 * @return Liste de ServerResponseDTO
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public List<ServerResponseDTO> getAllServers() {
		log.info("Appel REST pour lister tous les serveurs");
		
		try {
			List<ServerResponseDTO> servers = webClient.get()
					.uri("/api/servers")
					.retrieve()
					.bodyToFlux(ServerResponseDTO.class)
					.collectList()
					.block();
			
			log.debug("Réponse REST reçue pour getAllServers: nombre de serveurs={}", 
					servers != null ? servers.size() : 0);
			
			return servers != null ? servers : List.of();
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors de la récupération de la liste des serveurs: status={}, message={}", 
					e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour lister les serveurs: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la récupération de la liste des serveurs", e);
			throw new RuntimeException("Erreur inattendue lors de la récupération de la liste des serveurs", e);
		}
	}

	/**
	 * Renomme un serveur via le middle-service.
	 * 
	 * @param id L'ID du serveur à renommer
	 * @param request DTO contenant le nouveau nom
	 * @return ServerResponseDTO contenant le serveur renommé
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public ServerResponseDTO renameServer(Long id, RenameServerRequestDTO request) {
		log.info("Appel REST pour renommer le serveur: id={}, newName={}", id, request.getNewName());
		
		try {
			ServerResponseDTO response = webClient.put()
					.uri("/api/servers/{id}/rename", id)
					.contentType(MediaType.APPLICATION_JSON)
					.bodyValue(request)
					.retrieve()
					.bodyToMono(ServerResponseDTO.class)
					.block();
			
			log.debug("Réponse REST reçue pour renameServer: id={}, newName={}", id, request.getNewName());
			
			return response;
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors du renommage du serveur: id={}, status={}, message={}", 
					id, e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour renommer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors du renommage du serveur: id={}", id, e);
			throw new RuntimeException("Erreur inattendue lors du renommage du serveur", e);
		}
	}

	/**
	 * Récupère le statut d'un serveur via le middle-service.
	 * 
	 * @param id L'ID du serveur
	 * @return ServerResponseDTO contenant le statut du serveur
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public ServerResponseDTO getServerStatus(Long id) {
		log.info("Appel REST pour récupérer le statut du serveur: id={}", id);
		
		try {
			ServerResponseDTO response = webClient.get()
					.uri("/api/servers/{id}/status", id)
					.retrieve()
					.bodyToMono(ServerResponseDTO.class)
					.block();
			
			log.debug("Réponse REST reçue pour getServerStatus: id={}, status={}", 
					id, response != null ? response.getStatus() : null);
			
			return response;
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors de la récupération du statut du serveur: id={}, status={}, message={}", 
					id, e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour récupérer le statut du serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la récupération du statut du serveur: id={}", id, e);
			throw new RuntimeException("Erreur inattendue lors de la récupération du statut du serveur", e);
		}
	}

	/**
	 * Démarre un serveur via le middle-service.
	 * 
	 * @param id L'ID du serveur à démarrer
	 * @return ServerResponseDTO contenant le serveur démarré
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public ServerResponseDTO startServer(Long id) {
		log.info("Appel REST pour démarrer le serveur: id={}", id);
		
		try {
			ServerResponseDTO response = webClient.put()
					.uri("/api/servers/{id}/start", id)
					.retrieve()
					.bodyToMono(ServerResponseDTO.class)
					.block();
			
			log.debug("Réponse REST reçue pour startServer: id={}, status={}", 
					id, response != null ? response.getStatus() : null);
			
			return response;
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors du démarrage du serveur: id={}, status={}, message={}", 
					id, e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour démarrer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors du démarrage du serveur: id={}", id, e);
			throw new RuntimeException("Erreur inattendue lors du démarrage du serveur", e);
		}
	}

	/**
	 * Arrête un serveur via le middle-service.
	 * 
	 * @param id L'ID du serveur à arrêter
	 * @return ServerResponseDTO contenant le serveur arrêté
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public ServerResponseDTO stopServer(Long id) {
		log.info("Appel REST pour arrêter le serveur: id={}", id);
		
		try {
			ServerResponseDTO response = webClient.put()
					.uri("/api/servers/{id}/stop", id)
					.retrieve()
					.bodyToMono(ServerResponseDTO.class)
					.block();
			
			log.debug("Réponse REST reçue pour stopServer: id={}, status={}", 
					id, response != null ? response.getStatus() : null);
			
			return response;
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors de l'arrêt du serveur: id={}, status={}, message={}", 
					id, e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour arrêter le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de l'arrêt du serveur: id={}", id, e);
			throw new RuntimeException("Erreur inattendue lors de l'arrêt du serveur", e);
		}
	}

	/**
	 * Supprime un serveur via le middle-service.
	 * 
	 * @param id L'ID du serveur à supprimer
	 * @throws RuntimeException si l'appel HTTP échoue
	 */
	public void deleteServer(Long id) {
		log.info("Appel REST pour supprimer le serveur: id={}", id);
		
		try {
			webClient.delete()
					.uri("/api/servers/{id}", id)
					.retrieve()
					.toBodilessEntity()
					.block();
			
			log.debug("Réponse REST reçue pour deleteServer: id={}", id);
			
		} catch (WebClientResponseException e) {
			log.error("Erreur HTTP lors de la suppression du serveur: id={}, status={}, message={}", 
					id, e.getStatusCode(), e.getMessage(), e);
			throw new RuntimeException("Erreur lors de l'appel REST pour supprimer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la suppression du serveur: id={}", id, e);
			throw new RuntimeException("Erreur inattendue lors de la suppression du serveur", e);
		}
	}
}

