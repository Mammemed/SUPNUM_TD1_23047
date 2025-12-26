package com.supnum.middle.client;

import com.supnum.td1.servers.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.SoapFaultClientException;

/**
 * Client SOAP pour appeler le service SOAP existant.
 * 
 * Cette classe encapsule tous les appels SOAP vers le service de gestion des serveurs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SoapServerClient {

	private final WebServiceTemplate webServiceTemplate;
	
	@Value("${soap.service.url}")
	private String soapServiceUrl;

	/**
	 * Crée un nouveau serveur via le service SOAP.
	 * 
	 * @param name Le nom du serveur
	 * @param ipAddress L'adresse IP du serveur
	 * @return CreateServerResponse contenant le serveur créé
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public CreateServerResponse createServer(String name, String ipAddress) {
		log.info("Appel SOAP pour créer un serveur: name={}, ipAddress={}", name, ipAddress);
		
		try {
			CreateServerRequest request = new CreateServerRequest();
			request.setName(name);
			request.setIpAddress(ipAddress);
			
			CreateServerResponse response = (CreateServerResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour createServer: serverId={}, name={}", 
					response.getServer() != null ? response.getServer().getId() : null,
					response.getServer() != null ? response.getServer().getName() : null);
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors de la création du serveur: name={}, ipAddress={}", name, ipAddress, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour créer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la création du serveur: name={}, ipAddress={}", name, ipAddress, e);
			throw new RuntimeException("Erreur inattendue lors de la création du serveur", e);
		}
	}

	/**
	 * Liste tous les serveurs via le service SOAP.
	 * 
	 * @return ListServersResponse contenant la liste des serveurs
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public ListServersResponse listServers() {
		log.info("Appel SOAP pour lister tous les serveurs");
		
		try {
			ListServersRequest request = new ListServersRequest();
			
			ListServersResponse response = (ListServersResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour listServers: nombre de serveurs={}", 
					response.getServers() != null ? response.getServers().size() : 0);
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors de la récupération de la liste des serveurs", e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour lister les serveurs: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la récupération de la liste des serveurs", e);
			throw new RuntimeException("Erreur inattendue lors de la récupération de la liste des serveurs", e);
		}
	}

	/**
	 * Renomme un serveur via le service SOAP.
	 * 
	 * @param serverId L'ID du serveur à renommer
	 * @param newName Le nouveau nom du serveur
	 * @return RenameServerResponse contenant le serveur renommé
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public RenameServerResponse renameServer(Long serverId, String newName) {
		log.info("Appel SOAP pour renommer le serveur: id={}, newName={}", serverId, newName);
		
		try {
			RenameServerRequest request = new RenameServerRequest();
			request.setId(serverId);
			request.setNewName(newName);
			
			RenameServerResponse response = (RenameServerResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour renameServer: serverId={}, newName={}", 
					serverId, newName);
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors du renommage du serveur: id={}, newName={}", serverId, newName, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour renommer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors du renommage du serveur: id={}, newName={}", serverId, newName, e);
			throw new RuntimeException("Erreur inattendue lors du renommage du serveur", e);
		}
	}

	/**
	 * Récupère le statut d'un serveur via le service SOAP.
	 * 
	 * @param serverId L'ID du serveur
	 * @return GetServerStatusResponse contenant le statut du serveur
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public GetServerStatusResponse getServerStatus(Long serverId) {
		log.info("Appel SOAP pour récupérer le statut du serveur: id={}", serverId);
		
		try {
			GetServerStatusRequest request = new GetServerStatusRequest();
			request.setId(serverId);
			
			GetServerStatusResponse response = (GetServerStatusResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour getServerStatus: serverId={}, status={}", 
					serverId, response.isStatus());
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors de la récupération du statut du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour récupérer le statut du serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la récupération du statut du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur inattendue lors de la récupération du statut du serveur", e);
		}
	}

	/**
	 * Démarre un serveur via le service SOAP.
	 * 
	 * @param serverId L'ID du serveur à démarrer
	 * @return StartServerResponse contenant le serveur démarré
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public StartServerResponse startServer(Long serverId) {
		log.info("Appel SOAP pour démarrer le serveur: id={}", serverId);
		
		try {
			StartServerRequest request = new StartServerRequest();
			request.setId(serverId);
			
			StartServerResponse response = (StartServerResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour startServer: serverId={}, status={}", 
					serverId, 
					response.getServer() != null ? response.getServer().isStatus() : null);
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors du démarrage du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour démarrer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors du démarrage du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur inattendue lors du démarrage du serveur", e);
		}
	}

	/**
	 * Arrête un serveur via le service SOAP.
	 * 
	 * @param serverId L'ID du serveur à arrêter
	 * @return StopServerResponse contenant le serveur arrêté
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public StopServerResponse stopServer(Long serverId) {
		log.info("Appel SOAP pour arrêter le serveur: id={}", serverId);
		
		try {
			StopServerRequest request = new StopServerRequest();
			request.setId(serverId);
			
			StopServerResponse response = (StopServerResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour stopServer: serverId={}, status={}", 
					serverId,
					response.getServer() != null ? response.getServer().isStatus() : null);
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors de l'arrêt du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour arrêter le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de l'arrêt du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur inattendue lors de l'arrêt du serveur", e);
		}
	}

	/**
	 * Supprime un serveur via le service SOAP.
	 * 
	 * @param serverId L'ID du serveur à supprimer
	 * @return DeleteServerResponse indiquant si la suppression a réussi
	 * @throws RuntimeException si l'appel SOAP échoue
	 */
	public DeleteServerResponse deleteServer(Long serverId) {
		log.info("Appel SOAP pour supprimer le serveur: id={}", serverId);
		
		try {
			DeleteServerRequest request = new DeleteServerRequest();
			request.setId(serverId);
			
			DeleteServerResponse response = (DeleteServerResponse) webServiceTemplate
					.marshalSendAndReceive(soapServiceUrl, request);
			
			log.debug("Réponse SOAP reçue pour deleteServer: serverId={}, deleted={}", 
					serverId, response.isDeleted());
			
			return response;
		} catch (SoapFaultClientException e) {
			log.error("Erreur SOAP lors de la suppression du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur lors de l'appel SOAP pour supprimer le serveur: " + e.getMessage(), e);
		} catch (Exception e) {
			log.error("Erreur inattendue lors de la suppression du serveur: id={}", serverId, e);
			throw new RuntimeException("Erreur inattendue lors de la suppression du serveur", e);
		}
	}
}

