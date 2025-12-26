package com.supnum.middle.service;

import com.supnum.middle.client.SoapServerClient;
import com.supnum.middle.dto.CreateServerRequestDTO;
import com.supnum.middle.dto.RenameServerRequestDTO;
import com.supnum.middle.dto.ServerResponseDTO;
import com.supnum.td1.servers.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service REST qui orchestre les appels au service SOAP et convertit les réponses en DTOs REST.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ServerRestService {

	private final SoapServerClient soapServerClient;

	/**
	 * Crée un nouveau serveur.
	 * 
	 * @param request DTO contenant les informations du serveur à créer
	 * @return ServerResponseDTO contenant le serveur créé
	 */
	public ServerResponseDTO createServer(CreateServerRequestDTO request) {
		log.info("Création d'un serveur via le service REST: name={}, ipAddress={}", 
				request.getName(), request.getIpAddress());
		
		try {
			CreateServerResponse soapResponse = soapServerClient.createServer(
					request.getName(), 
					request.getIpAddress()
			);
			
			ServerResponseDTO response = convertToServerResponseDTO(soapResponse.getServer());
			response.setMessage("Serveur créé avec succès");
			
			log.info("Serveur créé avec succès: id={}, name={}", response.getId(), response.getName());
			return response;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors de la création du serveur: name={}, ipAddress={}", 
					request.getName(), request.getIpAddress(), e);
			throw new RuntimeException("Erreur lors de la création du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Récupère tous les serveurs.
	 * 
	 * @return Liste de ServerResponseDTO
	 */
	public List<ServerResponseDTO> getAllServers() {
		log.info("Récupération de tous les serveurs via le service REST");
		
		try {
			ListServersResponse soapResponse = soapServerClient.listServers();
			
			List<ServerResponseDTO> servers = soapResponse.getServers().stream()
					.map(this::convertToServerResponseDTO)
					.collect(Collectors.toList());
			
			// Ajouter un message à chaque serveur
			servers.forEach(server -> server.setMessage("Serveur récupéré avec succès"));
			
			log.info("Nombre de serveurs récupérés: {}", servers.size());
			return servers;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors de la récupération de la liste des serveurs", e);
			throw new RuntimeException("Erreur lors de la récupération de la liste des serveurs: " + e.getMessage(), e);
		}
	}

	/**
	 * Renomme un serveur.
	 * 
	 * @param id L'ID du serveur à renommer
	 * @param request DTO contenant le nouveau nom
	 * @return ServerResponseDTO contenant le serveur renommé
	 */
	public ServerResponseDTO renameServer(Long id, RenameServerRequestDTO request) {
		log.info("Renommage du serveur via le service REST: id={}, newName={}", id, request.getNewName());
		
		try {
			RenameServerResponse soapResponse = soapServerClient.renameServer(id, request.getNewName());
			
			ServerResponseDTO response = convertToServerResponseDTO(soapResponse.getServer());
			response.setMessage("Serveur renommé avec succès");
			
			log.info("Serveur renommé avec succès: id={}, newName={}", id, request.getNewName());
			return response;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors du renommage du serveur: id={}, newName={}", id, request.getNewName(), e);
			throw new RuntimeException("Erreur lors du renommage du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Récupère le statut d'un serveur.
	 * 
	 * @param id L'ID du serveur
	 * @return ServerResponseDTO contenant le statut du serveur
	 */
	public ServerResponseDTO getServerStatus(Long id) {
		log.info("Récupération du statut du serveur via le service REST: id={}", id);
		
		try {
			GetServerStatusResponse soapResponse = soapServerClient.getServerStatus(id);
			
			// Pour getServerStatus, on doit récupérer le serveur complet via listServers
			// car getServerStatus ne retourne qu'un boolean
			ListServersResponse listResponse = soapServerClient.listServers();
			Server server = listResponse.getServers().stream()
					.filter(s -> s.getId() == id)
					.findFirst()
					.orElseThrow(() -> new RuntimeException("Serveur non trouvé avec l'id: " + id));
			
			ServerResponseDTO response = convertToServerResponseDTO(server);
			response.setMessage("Statut du serveur récupéré avec succès. Statut: " + (soapResponse.isStatus() ? "En cours d'exécution" : "Arrêté"));
			
			log.info("Statut du serveur récupéré: id={}, status={}", id, soapResponse.isStatus());
			return response;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors de la récupération du statut du serveur: id={}", id, e);
			throw new RuntimeException("Erreur lors de la récupération du statut du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Démarre un serveur.
	 * 
	 * @param id L'ID du serveur à démarrer
	 * @return ServerResponseDTO contenant le serveur démarré
	 */
	public ServerResponseDTO startServer(Long id) {
		log.info("Démarrage du serveur via le service REST: id={}", id);
		
		try {
			StartServerResponse soapResponse = soapServerClient.startServer(id);
			
			ServerResponseDTO response = convertToServerResponseDTO(soapResponse.getServer());
			response.setMessage("Serveur démarré avec succès");
			
			log.info("Serveur démarré avec succès: id={}", id);
			return response;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors du démarrage du serveur: id={}", id, e);
			throw new RuntimeException("Erreur lors du démarrage du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Arrête un serveur.
	 * 
	 * @param id L'ID du serveur à arrêter
	 * @return ServerResponseDTO contenant le serveur arrêté
	 */
	public ServerResponseDTO stopServer(Long id) {
		log.info("Arrêt du serveur via le service REST: id={}", id);
		
		try {
			StopServerResponse soapResponse = soapServerClient.stopServer(id);
			
			ServerResponseDTO response = convertToServerResponseDTO(soapResponse.getServer());
			response.setMessage("Serveur arrêté avec succès");
			
			log.info("Serveur arrêté avec succès: id={}", id);
			return response;
			
		} catch (RuntimeException e) {
			log.error("Erreur lors de l'arrêt du serveur: id={}", id, e);
			throw new RuntimeException("Erreur lors de l'arrêt du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Supprime un serveur.
	 * 
	 * @param id L'ID du serveur à supprimer
	 */
	public void deleteServer(Long id) {
		log.info("Suppression du serveur via le service REST: id={}", id);
		
		try {
			DeleteServerResponse soapResponse = soapServerClient.deleteServer(id);
			
			if (soapResponse.isDeleted()) {
				log.info("Serveur supprimé avec succès: id={}", id);
			} else {
				log.warn("La suppression du serveur a échoué: id={}", id);
				throw new RuntimeException("La suppression du serveur a échoué pour l'id: " + id);
			}
			
		} catch (RuntimeException e) {
			log.error("Erreur lors de la suppression du serveur: id={}", id, e);
			throw new RuntimeException("Erreur lors de la suppression du serveur: " + e.getMessage(), e);
		}
	}

	/**
	 * Convertit un objet Server SOAP en ServerResponseDTO.
	 * 
	 * @param server L'objet Server SOAP
	 * @return ServerResponseDTO
	 */
	private ServerResponseDTO convertToServerResponseDTO(Server server) {
		if (server == null) {
			return null;
		}
		
		return ServerResponseDTO.builder()
				.id(server.getId())
				.name(server.getName())
				.ipAddress(server.getIpAddress())
				.status(server.isStatus())
				.build();
	}
}

