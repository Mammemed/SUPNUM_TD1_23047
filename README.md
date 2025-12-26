# 🚀 TD1 - Architecture SOA & WebServices - Partie B

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-blue.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-Academic-blue.svg)](LICENSE)

> **Projet académique** : Implémentation d'une architecture multi-services avec SOAP, REST et consommation de services

**Auteur** : SUPNUM - TD1 - Matricule 23047  
**Branche** : `SOA_TO_REST`

---

## 📋 Table des Matières

- [Architecture](#-architecture)
- [Structure du Projet](#-structure-du-projet)
- [Prérequis](#-prérequis)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Démarrage](#-démarrage)
- [URLs d'Accès](#-urls-daccès)
- [Documentation](#-documentation)
- [Tests](#-tests)
- [Technologies Utilisées](#-technologies-utilisées)

---

## 🏗️ Architecture

Ce projet implémente une architecture multi-services avec trois niveaux de communication :

```
┌─────────────────────────────────────────────────────────────────┐
│                         CLIENT                                  │
│                    (Consumer-Service)                           │
│                         Port 8082                               │
└────────────────────────────┬────────────────────────────────────┘
                              │
                              │ HTTP REST (WebClient)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    MIDDLE-SERVICE                               │
│              (Pont REST ↔ SOAP)                                 │
│                         Port 8081                               │
└────────────────────────────┬────────────────────────────────────┘
                              │
                              │ SOAP (WebServiceTemplate)
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      SOAP-SERVICE                               │
│            (Service Web SOAP)                                   │
│                         Port 8085                               │
└────────────────────────────┬────────────────────────────────────┘
                              │
                              │ JPA / Hibernate
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    POSTGRESQL DATABASE                          │
│                  (Port 5432)                                    │
└─────────────────────────────────────────────────────────────────┘
```

### Flux de Communication

```
Client → Consumer-Service (8082) → Middle-Service (8081) → SOAP-Service (8085) → Database
```

**Description** :
- **Consumer-Service** : Service client qui consomme le Middle-Service via REST
- **Middle-Service** : Service intermédiaire qui expose une API REST et consomme le SOAP-Service
- **SOAP-Service** : Service backend qui expose une API SOAP et gère la persistance

---

## 📁 Structure du Projet

```
SUPNUM_TD1_23047/
├── soap-service/              # Service SOAP (Backend)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/supnum/td1_servers/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── middle-service/            # Service Intermédiaire (REST ↔ SOAP)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/supnum/middle/
│   │   │   │   ├── client/      # Client SOAP
│   │   │   │   ├── config/      # Configuration
│   │   │   │   ├── controller/  # Controllers REST
│   │   │   │   ├── dto/         # DTOs REST
│   │   │   │   ├── exception/   # Gestion d'erreurs
│   │   │   │   └── service/     # Services métier
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── consumer-service/          # Service Client (Consomme REST)
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/supnum/consumer/
│   │   │   │   ├── client/      # Client REST
│   │   │   │   ├── config/      # Configuration
│   │   │   │   ├── dto/         # DTOs
│   │   │   │   ├── runner/      # Tests automatiques
│   │   │   │   └── service/     # Services
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── README.md
│
├── docs/                      # Documentation
│   ├── CONTRAT_SOAP.md
│   ├── CONTRAT_REST_MIDDLE.md
│   └── CONTRAT_REST_CONSUMER.md
│
└── README.md                  # Ce fichier
```

---

## ✅ Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- ☕ **Java 17** ou supérieur
  ```bash
  java -version
  ```

- 📦 **Maven 3.9+**
  ```bash
  mvn -version
  ```

- 🐘 **PostgreSQL** (ou MySQL)
  - PostgreSQL 12+ recommandé
  - Base de données créée : `servers_db`
  - Port par défaut : `5432`

- 🌐 **Accès Internet** (pour télécharger les dépendances Maven)

---

## 🔧 Installation

### 1. Cloner le projet

```bash
git clone <repository-url>
cd SUPNUM_TD1_23047
```

### 2. Configurer la base de données

#### PostgreSQL

```sql
-- Créer la base de données
CREATE DATABASE servers_db;

-- Créer un utilisateur (optionnel)
CREATE USER postgres WITH PASSWORD 'mamme6';
GRANT ALL PRIVILEGES ON DATABASE servers_db TO postgres;
```

#### Configuration dans `soap-service/src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/servers_db
spring.datasource.username=postgres
spring.datasource.password=mamme6
```

---

## ⚙️ Configuration

### SOAP-Service

**Fichier** : `soap-service/src/main/resources/application.properties`

```properties
server.port=8085
spring.datasource.url=jdbc:postgresql://localhost:5432/servers_db
spring.datasource.username=postgres
spring.datasource.password=mamme6
```

### Middle-Service

**Fichier** : `middle-service/src/main/resources/application.yml`

```yaml
server:
  port: 8081

spring:
  application:
    name: middle-service

soap:
  service:
    url: http://localhost:8085/ws
```

### Consumer-Service

**Fichier** : `consumer-service/src/main/resources/application.yml`

```yaml
server:
  port: 8082

spring:
  application:
    name: consumer-service

middle:
  service:
    url: http://localhost:8081
```

---

## 🚀 Démarrage

⚠️ **IMPORTANT** : Les services doivent être démarrés dans l'ordre suivant :

### 1. Démarrer le SOAP-Service

```bash
cd soap-service
mvn clean install
mvn spring-boot:run
```

✅ Le service démarre sur le port **8085**

### 2. Démarrer le Middle-Service

Dans un nouveau terminal :

```bash
cd middle-service
mvn clean install
mvn spring-boot:run
```

✅ Le service démarre sur le port **8081**

### 3. Démarrer le Consumer-Service

Dans un nouveau terminal :

```bash
cd consumer-service
mvn clean install
mvn spring-boot:run
```

✅ Le service démarre sur le port **8082** et exécute automatiquement les tests

---

## 🌐 URLs d'Accès

### SOAP-Service (Port 8085)

- 🔗 **WSDL** : [http://localhost:8085/ws/servers.wsdl](http://localhost:8085/ws/servers.wsdl)
- 🔗 **Endpoint SOAP** : `http://localhost:8085/ws`

### Middle-Service (Port 8081)

- 🔗 **API REST** : [http://localhost:8081/api/servers](http://localhost:8081/api/servers)
- 🔗 **Swagger UI** : [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- 🔗 **API Docs JSON** : [http://localhost:8081/api-docs](http://localhost:8081/api-docs)

### Consumer-Service (Port 8082)

- 🔗 **Service** : [http://localhost:8082](http://localhost:8082)

---

## 📚 Documentation

La documentation complète est disponible dans le dossier `docs/` :

- 📄 **[CONTRAT_SOAP.md](docs/CONTRAT_SOAP.md)** - Documentation du service SOAP
  - Toutes les opérations SOAP
  - Exemples de requêtes/réponses XML
  - Structure du WSDL

- 📄 **[CONTRAT_REST_MIDDLE.md](docs/CONTRAT_REST_MIDDLE.md)** - Documentation du Middle-Service REST API
  - Tous les endpoints REST
  - Exemples JSON et cURL
  - Codes de réponse HTTP

- 📄 **[CONTRAT_REST_CONSUMER.md](docs/CONTRAT_REST_CONSUMER.md)** - Documentation du Consumer-Service
  - Architecture de communication
  - Utilisation du client REST
  - Exemples de code

---

## 🧪 Tests

### Tests Automatiques

Le **Consumer-Service** exécute automatiquement un scénario de test complet au démarrage :

1. ✅ Créer un serveur
2. ✅ Lister tous les serveurs
3. ✅ Vérifier le statut
4. ✅ Démarrer le serveur
5. ✅ Renommer le serveur
6. ✅ Arrêter le serveur
7. ✅ Supprimer le serveur

Les résultats sont affichés dans les logs du Consumer-Service.

### Tests Manuels

#### Tester le SOAP-Service

```bash
# Créer un serveur via SOAP
curl -X POST http://localhost:8085/ws \
  -H "Content-Type: text/xml;charset=UTF-8" \
  -H "SOAPAction: \"\"" \
  -d @soap-request.xml
```

#### Tester le Middle-Service

```bash
# Créer un serveur via REST
curl -X POST http://localhost:8081/api/servers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Serveur-Test",
    "ipAddress": "192.168.1.100"
  }'
```

#### Tester le Consumer-Service

Le Consumer-Service teste automatiquement toute la chaîne au démarrage. Consultez les logs pour voir les résultats.

---

## 🛠️ Technologies Utilisées

### Backend

- ☕ **Java 17**
- 🍃 **Spring Boot 3.5.7**
  - Spring Web Services (SOAP)
  - Spring Web (REST)
  - Spring WebFlux (WebClient)
  - Spring Data JPA
- 🗄️ **PostgreSQL** (Base de données)
- 🔧 **Maven** (Gestion des dépendances)

### Outils & Bibliothèques

- 📝 **Lombok** - Réduction du code boilerplate
- ✅ **Jakarta Validation** - Validation des données
- 📖 **Springdoc OpenAPI** - Documentation Swagger
- 🧪 **JUnit** - Framework de tests

### Build

- 🔨 **Maven Wrapper** - Build sans installation Maven globale

---

## 📊 Résumé des Services

| Service | Port | Type | Description |
|---------|------|------|-------------|
| **SOAP-Service** | 8085 | SOAP | Service backend avec persistance |
| **Middle-Service** | 8081 | REST | Pont entre REST et SOAP |
| **Consumer-Service** | 8082 | Client | Consomme le Middle-Service |

---

## 🎯 Fonctionnalités

### Gestion des Serveurs

- ✅ Créer un serveur
- ✅ Lister tous les serveurs
- ✅ Récupérer le statut d'un serveur
- ✅ Démarrer un serveur
- ✅ Arrêter un serveur
- ✅ Renommer un serveur
- ✅ Supprimer un serveur

### Validation

- ✅ Validation des adresses IP
- ✅ Validation des champs obligatoires
- ✅ Gestion des erreurs standardisées

### Documentation

- ✅ Documentation Swagger/OpenAPI
- ✅ Documentation SOAP (WSDL)
- ✅ Documentation complète dans `docs/`

---

## 🐛 Dépannage

### Le SOAP-Service ne démarre pas

- ✅ Vérifiez que PostgreSQL est démarré
- ✅ Vérifiez les credentials dans `application.properties`
- ✅ Vérifiez que le port 8085 est libre

### Le Middle-Service ne peut pas appeler le SOAP-Service

- ✅ Vérifiez que le SOAP-Service est démarré sur le port 8085
- ✅ Vérifiez l'URL dans `application.yml` : `soap.service.url`

### Le Consumer-Service ne peut pas appeler le Middle-Service

- ✅ Vérifiez que le Middle-Service est démarré sur le port 8081
- ✅ Vérifiez l'URL dans `application.yml` : `middle.service.url`

---

## 📝 Notes

- Les services doivent être démarrés dans l'ordre : SOAP → Middle → Consumer
- Le Consumer-Service exécute automatiquement des tests au démarrage
- La documentation Swagger est disponible sur le Middle-Service
- Tous les services log leurs opérations pour faciliter le débogage

---

## 👤 Auteur

**SUPNUM - TD1 - Matricule 23047**

**Branche** : `SOA_TO_REST`

---

## 📄 License

Ce projet est réalisé dans le cadre d'un travail académique (TD1 - Architecture SOA & WebServices).

---

## 🙏 Remerciements

- Spring Framework pour les outils de développement
- La communauté open-source pour les bibliothèques utilisées

---

**Dernière mise à jour** : Décembre 2025

