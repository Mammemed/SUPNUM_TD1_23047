# Contrat REST - Middle Service API

## Informations Générales

- **URL de base** : `http://localhost:8081/api/servers`
- **Port** : `8081`
- **Documentation Swagger** : `http://localhost:8081/swagger-ui.html`
- **API Docs JSON** : `http://localhost:8081/api-docs`

## Architecture

Le middle-service agit comme un pont entre les clients REST et le service SOAP :
- **Consomme** : Service SOAP (port 8085)
- **Expose** : API REST (port 8081)

---

## Endpoints REST

### 1. Créer un serveur

Crée un nouveau serveur dans le système.

**Endpoint** : `POST /api/servers`

**Headers** :
```
Content-Type: application/json
```

**Body** :
```json
{
  "name": "Serveur-Web-1",
  "ipAddress": "192.168.1.100"
}
```

**Validation** :
- `name` : Obligatoire, non vide
- `ipAddress` : Obligatoire, format IP valide (regex: `^([0-9]{1,3}\.){3}[0-9]{1,3}$`)

**Réponse** : `201 Created`

```json
{
  "id": 1,
  "name": "Serveur-Web-1",
  "ipAddress": "192.168.1.100",
  "status": false,
  "message": "Serveur créé avec succès"
}
```

**Codes de réponse** :
- `201` : Serveur créé avec succès
- `400` : Requête invalide (validation échouée)
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X POST http://localhost:8081/api/servers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Serveur-Web-1",
    "ipAddress": "192.168.1.100"
  }'
```

---

### 2. Lister tous les serveurs

Récupère la liste de tous les serveurs.

**Endpoint** : `GET /api/servers`

**Réponse** : `200 OK`

```json
[
  {
    "id": 1,
    "name": "Serveur-Web-1",
    "ipAddress": "192.168.1.100",
    "status": false,
    "message": "Serveur récupéré avec succès"
  },
  {
    "id": 2,
    "name": "Serveur-DB-1",
    "ipAddress": "192.168.1.101",
    "status": true,
    "message": "Serveur récupéré avec succès"
  }
]
```

**Codes de réponse** :
- `200` : Liste récupérée avec succès
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X GET http://localhost:8081/api/servers
```

---

### 3. Renommer un serveur

Renomme un serveur existant.

**Endpoint** : `PUT /api/servers/{id}/rename`

**Path Parameters** :
- `id` (Long) : ID du serveur à renommer

**Headers** :
```
Content-Type: application/json
```

**Body** :
```json
{
  "newName": "Serveur-Web-Renommé"
}
```

**Validation** :
- `newName` : Obligatoire, non vide

**Réponse** : `200 OK`

```json
{
  "id": 1,
  "name": "Serveur-Web-Renommé",
  "ipAddress": "192.168.1.100",
  "status": false,
  "message": "Serveur renommé avec succès"
}
```

**Codes de réponse** :
- `200` : Serveur renommé avec succès
- `400` : Requête invalide (validation échouée)
- `404` : Serveur non trouvé
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X PUT http://localhost:8081/api/servers/1/rename \
  -H "Content-Type: application/json" \
  -d '{
    "newName": "Serveur-Web-Renommé"
  }'
```

---

### 4. Obtenir le statut d'un serveur

Récupère le statut d'un serveur.

**Endpoint** : `GET /api/servers/{id}/status`

**Path Parameters** :
- `id` (Long) : ID du serveur

**Réponse** : `200 OK`

```json
{
  "id": 1,
  "name": "Serveur-Web-1",
  "ipAddress": "192.168.1.100",
  "status": true,
  "message": "Statut du serveur récupéré avec succès. Statut: En cours d'exécution"
}
```

**Codes de réponse** :
- `200` : Statut récupéré avec succès
- `404` : Serveur non trouvé
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X GET http://localhost:8081/api/servers/1/status
```

---

### 5. Démarrer un serveur

Démarre un serveur arrêté.

**Endpoint** : `PUT /api/servers/{id}/start`

**Path Parameters** :
- `id` (Long) : ID du serveur à démarrer

**Réponse** : `200 OK`

```json
{
  "id": 1,
  "name": "Serveur-Web-1",
  "ipAddress": "192.168.1.100",
  "status": true,
  "message": "Serveur démarré avec succès"
}
```

**Codes de réponse** :
- `200` : Serveur démarré avec succès
- `404` : Serveur non trouvé
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X PUT http://localhost:8081/api/servers/1/start
```

---

### 6. Arrêter un serveur

Arrête un serveur en cours d'exécution.

**Endpoint** : `PUT /api/servers/{id}/stop`

**Path Parameters** :
- `id` (Long) : ID du serveur à arrêter

**Réponse** : `200 OK`

```json
{
  "id": 1,
  "name": "Serveur-Web-1",
  "ipAddress": "192.168.1.100",
  "status": false,
  "message": "Serveur arrêté avec succès"
}
```

**Codes de réponse** :
- `200` : Serveur arrêté avec succès
- `404` : Serveur non trouvé
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X PUT http://localhost:8081/api/servers/1/stop
```

---

### 7. Supprimer un serveur

Supprime un serveur du système.

**Endpoint** : `DELETE /api/servers/{id}`

**Path Parameters** :
- `id` (Long) : ID du serveur à supprimer

**Réponse** : `204 No Content`

**Codes de réponse** :
- `204` : Serveur supprimé avec succès
- `400` : Le serveur est en cours d'exécution et ne peut pas être supprimé
- `404` : Serveur non trouvé
- `500` : Erreur serveur

**Exemple cURL** :
```bash
curl -X DELETE http://localhost:8081/api/servers/1
```

**Note importante** : Le serveur doit être arrêté (status = false) avant de pouvoir être supprimé.

---

## Modèles de Données

### CreateServerRequestDTO

```json
{
  "name": "string (obligatoire, non vide)",
  "ipAddress": "string (obligatoire, format IP valide)"
}
```

### RenameServerRequestDTO

```json
{
  "newName": "string (obligatoire, non vide)"
}
```

### ServerResponseDTO

```json
{
  "id": "long",
  "name": "string",
  "ipAddress": "string",
  "status": "boolean (true = en cours d'exécution, false = arrêté)",
  "message": "string"
}
```

### ErrorResponse

En cas d'erreur, le service retourne :

```json
{
  "timestamp": "2025-12-26T23:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Erreur de validation: name: Le nom du serveur est obligatoire",
  "path": "/api/servers"
}
```

---

## Gestion des Erreurs

Le middle-service gère les erreurs de manière standardisée :

- **400 Bad Request** : Erreurs de validation (champs manquants ou invalides)
- **404 Not Found** : Ressource non trouvée
- **500 Internal Server Error** : Erreurs serveur (erreurs SOAP, erreurs de base de données, etc.)

Toutes les erreurs retournent un objet `ErrorResponse` avec les détails de l'erreur.

---

## Flux de Communication

```
Client REST → Middle-Service (REST) → SOAP-Service → Database
                ↓
            Conversion DTO
                ↓
            Appel SOAP
                ↓
            Conversion Response
                ↓
            Retour REST
```

---

## Notes Techniques

- Le service utilise Spring WebFlux pour les appels SOAP
- Les DTOs utilisent Jakarta Validation pour la validation
- La documentation Swagger est disponible à `/swagger-ui.html`
- Le service log toutes les requêtes et réponses pour le débogage

