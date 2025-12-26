# Contrat REST - Consumer Service

## Informations Générales

- **Port** : `8082`
- **URL du Middle-Service** : `http://localhost:8081`
- **Rôle** : Consomme le Middle-Service REST API

## Architecture de Communication

Le consumer-service est un service client qui consomme le middle-service REST API pour effectuer des opérations sur les serveurs.

```
Consumer-Service (Port 8082)
    ↓ (HTTP REST)
Middle-Service (Port 8081)
    ↓ (SOAP)
SOAP-Service (Port 8085)
    ↓
Database
```

## Architecture Technique

### Composants Principaux

1. **WebClientConfig** : Configuration du WebClient pour appeler le middle-service
2. **MiddleServiceClient** : Client REST qui encapsule tous les appels HTTP
3. **TestRunner** : CommandLineRunner qui exécute des tests au démarrage
4. **DTOs** : Modèles de données identiques au middle-service

### Flux de Communication

```
┌─────────────────────┐
│  Consumer-Service   │
│   (Port 8082)       │
└──────────┬──────────┘
           │
           │ HTTP REST (WebClient)
           │
           ▼
┌─────────────────────┐
│  Middle-Service     │
│   (Port 8081)       │
└──────────┬──────────┘
           │
           │ SOAP (WebServiceTemplate)
           │
           ▼
┌─────────────────────┐
│  SOAP-Service       │
│   (Port 8085)       │
└──────────┬──────────┘
           │
           │ JPA/Hibernate
           │
           ▼
┌─────────────────────┐
│   PostgreSQL DB     │
└─────────────────────┘
```

## Utilisation du MiddleServiceClient

Le `MiddleServiceClient` expose les mêmes méthodes que les endpoints REST du middle-service :

### 1. Créer un serveur

```java
CreateServerRequestDTO request = CreateServerRequestDTO.builder()
    .name("Serveur-Web-1")
    .ipAddress("192.168.1.100")
    .build();

ServerResponseDTO response = middleServiceClient.createServer(request);
```

**Appel HTTP** : `POST http://localhost:8081/api/servers`

### 2. Lister tous les serveurs

```java
List<ServerResponseDTO> servers = middleServiceClient.getAllServers();
```

**Appel HTTP** : `GET http://localhost:8081/api/servers`

### 3. Renommer un serveur

```java
RenameServerRequestDTO request = RenameServerRequestDTO.builder()
    .newName("Serveur-Renommé")
    .build();

ServerResponseDTO response = middleServiceClient.renameServer(1L, request);
```

**Appel HTTP** : `PUT http://localhost:8081/api/servers/1/rename`

### 4. Obtenir le statut

```java
ServerResponseDTO response = middleServiceClient.getServerStatus(1L);
```

**Appel HTTP** : `GET http://localhost:8081/api/servers/1/status`

### 5. Démarrer un serveur

```java
ServerResponseDTO response = middleServiceClient.startServer(1L);
```

**Appel HTTP** : `PUT http://localhost:8081/api/servers/1/start`

### 6. Arrêter un serveur

```java
ServerResponseDTO response = middleServiceClient.stopServer(1L);
```

**Appel HTTP** : `PUT http://localhost:8081/api/servers/1/stop`

### 7. Supprimer un serveur

```java
middleServiceClient.deleteServer(1L);
```

**Appel HTTP** : `DELETE http://localhost:8081/api/servers/1`

## TestRunner - Scénario de Test Automatique

Le `TestRunner` exécute automatiquement un scénario de test complet au démarrage de l'application :

### Scénario de Test

1. **Test 1** : Créer un serveur
   - Crée un serveur avec le nom "Serveur-Test-1" et l'IP "192.168.1.100"
   - Stocke l'ID pour les tests suivants

2. **Test 2** : Lister tous les serveurs
   - Récupère et affiche la liste complète des serveurs

3. **Test 3** : Vérifier le statut
   - Récupère le statut du serveur créé

4. **Test 4** : Démarrer le serveur
   - Démarre le serveur créé
   - Vérifie que le statut passe à "en cours d'exécution"

5. **Test 5** : Renommer le serveur
   - Renomme le serveur en "Serveur-Test-Renommé"

6. **Test 6** : Arrêter le serveur
   - Arrête le serveur
   - Vérifie que le statut passe à "arrêté"

7. **Test 7** : Supprimer le serveur
   - Supprime le serveur créé

### Exécution

Au démarrage du consumer-service, les tests s'exécutent automatiquement et affichent des logs détaillés montrant toute la chaîne de communication.

## Configuration

### application.yml

```yaml
server:
  port: 8082

spring:
  application:
    name: consumer-service

middle:
  service:
    url: http://localhost:8081

logging:
  level:
    root: INFO
    com.supnum.consumer: DEBUG
```

### WebClientConfig

Le WebClient est configuré avec l'URL de base du middle-service :

```java
@Bean
public WebClient webClient() {
    return WebClient.builder()
            .baseUrl(middleServiceUrl)  // http://localhost:8081
            .build();
}
```

## Gestion des Erreurs

Le `MiddleServiceClient` gère les erreurs HTTP :

- **WebClientResponseException** : Erreurs HTTP (400, 404, 500, etc.)
- **Exception** : Erreurs inattendues

Toutes les erreurs sont loggées avec le statut HTTP et le message d'erreur, puis propagées avec un message explicite.

## Logs

Le consumer-service log toutes les opérations :

- **INFO** : Chaque appel REST avec les paramètres
- **DEBUG** : Les réponses reçues
- **ERROR** : Les erreurs avec stack trace

Exemple de log :
```
INFO  - Appel REST pour créer un serveur: name=Serveur-Web-1, ipAddress=192.168.1.100
DEBUG - Réponse REST reçue pour createServer: id=1, name=Serveur-Web-1
```

## Exemple d'Utilisation Complète

### Créer et gérer un serveur

```java
@Component
@RequiredArgsConstructor
public class MyService {
    
    private final MiddleServiceClient client;
    
    public void manageServer() {
        // 1. Créer un serveur
        CreateServerRequestDTO createRequest = CreateServerRequestDTO.builder()
            .name("Mon-Serveur")
            .ipAddress("192.168.1.50")
            .build();
        
        ServerResponseDTO created = client.createServer(createRequest);
        Long serverId = created.getId();
        
        // 2. Démarrer le serveur
        ServerResponseDTO started = client.startServer(serverId);
        
        // 3. Vérifier le statut
        ServerResponseDTO status = client.getServerStatus(serverId);
        
        // 4. Arrêter le serveur
        ServerResponseDTO stopped = client.stopServer(serverId);
        
        // 5. Supprimer le serveur
        client.deleteServer(serverId);
    }
}
```

## Dépendances

Le consumer-service utilise :

- **Spring Web** : Pour les fonctionnalités web
- **Spring WebFlux** : Pour WebClient (appels HTTP réactifs)
- **Lombok** : Pour réduire le code boilerplate
- **Spring Boot DevTools** : Pour le développement

## Notes Techniques

- Le WebClient utilise une approche réactive mais bloque les appels avec `.block()` pour simplifier l'utilisation
- Les DTOs sont identiques à ceux du middle-service pour assurer la compatibilité
- Le TestRunner s'exécute automatiquement au démarrage (peut être désactivé si nécessaire)
- Tous les appels sont loggés pour faciliter le débogage

## Démarrage des Services

Pour tester la chaîne complète :

1. **Démarrer le SOAP-Service** (port 8085)
   ```bash
   cd soap-service
   mvn spring-boot:run
   ```

2. **Démarrer le Middle-Service** (port 8081)
   ```bash
   cd middle-service
   mvn spring-boot:run
   ```

3. **Démarrer le Consumer-Service** (port 8082)
   ```bash
   cd consumer-service
   mvn spring-boot:run
   ```

Le consumer-service exécutera automatiquement les tests au démarrage et affichera les résultats dans les logs.

