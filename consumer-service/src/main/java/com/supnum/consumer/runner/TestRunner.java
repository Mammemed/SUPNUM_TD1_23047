package com.supnum.consumer.runner;

import com.supnum.consumer.client.MiddleServiceClient;
import com.supnum.consumer.dto.CreateServerRequestDTO;
import com.supnum.consumer.dto.RenameServerRequestDTO;
import com.supnum.consumer.dto.ServerResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CommandLineRunner pour tester la chaîne complète de communication :
 * Consumer-Service → Middle-Service → SOAP-Service → Database
 * 
 * Cette classe exécute un scénario de test complet au démarrage de l'application.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TestRunner implements CommandLineRunner {

	private final MiddleServiceClient middleServiceClient;
	
	private Long createdServerId;

	@Override
	public void run(String... args) throws Exception {
		log.info("=========================================");
		log.info("DÉMARRAGE DES TESTS DE LA CHAÎNE COMPLÈTE");
		log.info("Consumer → Middle-Service → SOAP-Service → Database");
		log.info("=========================================");
		
		try {
			// Test 1 : Créer un serveur
			testCreateServer();
			
			// Test 2 : Lister tous les serveurs
			testGetAllServers();
			
			// Test 3 : Vérifier le statut
			testGetServerStatus();
			
			// Test 4 : Démarrer le serveur
			testStartServer();
			
			// Test 5 : Renommer le serveur
			testRenameServer();
			
			// Test 6 : Arrêter le serveur
			testStopServer();
			
			// Test 7 : Supprimer le serveur
			testDeleteServer();
			
			log.info("=========================================");
			log.info("TOUS LES TESTS SONT TERMINÉS AVEC SUCCÈS");
			log.info("=========================================");
			
		} catch (Exception e) {
			log.error("=========================================");
			log.error("ERREUR LORS DE L'EXÉCUTION DES TESTS", e);
			log.error("=========================================");
		}
	}

	/**
	 * Test 1 : Créer un serveur
	 */
	private void testCreateServer() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 1 : CRÉER UN SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		try {
			CreateServerRequestDTO request = CreateServerRequestDTO.builder()
					.name("Serveur-Test-1")
					.ipAddress("192.168.1.100")
					.build();
			
			log.info("📤 Consumer-Service : Envoi de la requête POST /api/servers");
			log.info("   → Nom: {}, IP: {}", request.getName(), request.getIpAddress());
			
			ServerResponseDTO response = middleServiceClient.createServer(request);
			
			if (response != null) {
				createdServerId = response.getId();
				log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
				log.info("   → ID: {}", response.getId());
				log.info("   → Nom: {}", response.getName());
				log.info("   → IP: {}", response.getIpAddress());
				log.info("   → Statut: {}", response.getStatus() ? "En cours d'exécution" : "Arrêté");
				log.info("   → Message: {}", response.getMessage());
				log.info("✅ TEST 1 RÉUSSI : Serveur créé avec succès (ID: {})", createdServerId);
			} else {
				log.error("❌ TEST 1 ÉCHOUÉ : Réponse nulle");
			}
			
		} catch (Exception e) {
			log.error("❌ TEST 1 ÉCHOUÉ : Erreur lors de la création du serveur", e);
			throw new RuntimeException("Test 1 échoué", e);
		}
	}

	/**
	 * Test 2 : Lister tous les serveurs
	 */
	private void testGetAllServers() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 2 : LISTER TOUS LES SERVEURS");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		try {
			log.info("📤 Consumer-Service : Envoi de la requête GET /api/servers");
			
			List<ServerResponseDTO> servers = middleServiceClient.getAllServers();
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → Nombre de serveurs: {}", servers != null ? servers.size() : 0);
			
			if (servers != null && !servers.isEmpty()) {
				log.info("   → Liste des serveurs:");
				servers.forEach(server -> {
					log.info("      • ID: {}, Nom: {}, IP: {}, Statut: {}", 
							server.getId(), 
							server.getName(), 
							server.getIpAddress(),
							server.getStatus() ? "En cours d'exécution" : "Arrêté");
				});
			}
			
			log.info("✅ TEST 2 RÉUSSI : Liste des serveurs récupérée avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 2 ÉCHOUÉ : Erreur lors de la récupération de la liste", e);
			throw new RuntimeException("Test 2 échoué", e);
		}
	}

	/**
	 * Test 3 : Vérifier le statut
	 */
	private void testGetServerStatus() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 3 : VÉRIFIER LE STATUT DU SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		if (createdServerId == null) {
			log.warn("⚠️  TEST 3 SKIPPÉ : Aucun serveur créé précédemment");
			return;
		}
		
		try {
			log.info("📤 Consumer-Service : Envoi de la requête GET /api/servers/{}/status", createdServerId);
			
			ServerResponseDTO response = middleServiceClient.getServerStatus(createdServerId);
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → ID: {}", response.getId());
			log.info("   → Statut: {}", response.getStatus() ? "En cours d'exécution" : "Arrêté");
			log.info("   → Message: {}", response.getMessage());
			log.info("✅ TEST 3 RÉUSSI : Statut du serveur récupéré avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 3 ÉCHOUÉ : Erreur lors de la récupération du statut", e);
			throw new RuntimeException("Test 3 échoué", e);
		}
	}

	/**
	 * Test 4 : Démarrer le serveur
	 */
	private void testStartServer() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 4 : DÉMARRER LE SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		if (createdServerId == null) {
			log.warn("⚠️  TEST 4 SKIPPÉ : Aucun serveur créé précédemment");
			return;
		}
		
		try {
			log.info("📤 Consumer-Service : Envoi de la requête PUT /api/servers/{}/start", createdServerId);
			
			ServerResponseDTO response = middleServiceClient.startServer(createdServerId);
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → ID: {}", response.getId());
			log.info("   → Statut: {}", response.getStatus() ? "En cours d'exécution" : "Arrêté");
			log.info("   → Message: {}", response.getMessage());
			log.info("✅ TEST 4 RÉUSSI : Serveur démarré avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 4 ÉCHOUÉ : Erreur lors du démarrage du serveur", e);
			throw new RuntimeException("Test 4 échoué", e);
		}
	}

	/**
	 * Test 5 : Renommer le serveur
	 */
	private void testRenameServer() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 5 : RENOMMER LE SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		if (createdServerId == null) {
			log.warn("⚠️  TEST 5 SKIPPÉ : Aucun serveur créé précédemment");
			return;
		}
		
		try {
			RenameServerRequestDTO request = RenameServerRequestDTO.builder()
					.newName("Serveur-Test-Renommé")
					.build();
			
			log.info("📤 Consumer-Service : Envoi de la requête PUT /api/servers/{}/rename", createdServerId);
			log.info("   → Nouveau nom: {}", request.getNewName());
			
			ServerResponseDTO response = middleServiceClient.renameServer(createdServerId, request);
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → ID: {}", response.getId());
			log.info("   → Nouveau nom: {}", response.getName());
			log.info("   → Message: {}", response.getMessage());
			log.info("✅ TEST 5 RÉUSSI : Serveur renommé avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 5 ÉCHOUÉ : Erreur lors du renommage du serveur", e);
			throw new RuntimeException("Test 5 échoué", e);
		}
	}

	/**
	 * Test 6 : Arrêter le serveur
	 */
	private void testStopServer() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 6 : ARRÊTER LE SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		if (createdServerId == null) {
			log.warn("⚠️  TEST 6 SKIPPÉ : Aucun serveur créé précédemment");
			return;
		}
		
		try {
			log.info("📤 Consumer-Service : Envoi de la requête PUT /api/servers/{}/stop", createdServerId);
			
			ServerResponseDTO response = middleServiceClient.stopServer(createdServerId);
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → ID: {}", response.getId());
			log.info("   → Statut: {}", response.getStatus() ? "En cours d'exécution" : "Arrêté");
			log.info("   → Message: {}", response.getMessage());
			log.info("✅ TEST 6 RÉUSSI : Serveur arrêté avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 6 ÉCHOUÉ : Erreur lors de l'arrêt du serveur", e);
			throw new RuntimeException("Test 6 échoué", e);
		}
	}

	/**
	 * Test 7 : Supprimer le serveur
	 */
	private void testDeleteServer() {
		log.info("");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("TEST 7 : SUPPRIMER LE SERVEUR");
		log.info("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━");
		log.info("Chaîne : Consumer → Middle-Service → SOAP-Service → Database");
		
		if (createdServerId == null) {
			log.warn("⚠️  TEST 7 SKIPPÉ : Aucun serveur créé précédemment");
			return;
		}
		
		try {
			log.info("📤 Consumer-Service : Envoi de la requête DELETE /api/servers/{}", createdServerId);
			
			middleServiceClient.deleteServer(createdServerId);
			
			log.info("✅ Consumer-Service : Réponse reçue du Middle-Service");
			log.info("   → Serveur supprimé avec succès");
			log.info("✅ TEST 7 RÉUSSI : Serveur supprimé avec succès");
			
		} catch (Exception e) {
			log.error("❌ TEST 7 ÉCHOUÉ : Erreur lors de la suppression du serveur", e);
			throw new RuntimeException("Test 7 échoué", e);
		}
	}
}

