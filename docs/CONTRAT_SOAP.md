# Contrat SOAP - Service de Gestion des Serveurs

## Informations Générales

- **URL du WSDL** : `http://localhost:8085/ws/servers.wsdl`
- **Namespace** : `http://supnum.com/td1/servers`
- **Port** : `8085`
- **Endpoint** : `http://localhost:8085/ws`

## Structure des Types

### Type Server

Le type `Server` représente un serveur dans le système.

```xml
<xs:complexType name="Server">
    <xs:sequence>
        <xs:element name="id" type="xs:long"/>
        <xs:element name="name" type="xs:string"/>
        <xs:element name="ipAddress" type="xs:string"/>
        <xs:element name="status" type="xs:boolean"/>
    </xs:sequence>
</xs:complexType>
```

**Champs** :
- `id` (long) : Identifiant unique du serveur
- `name` (string) : Nom du serveur
- `ipAddress` (string) : Adresse IP du serveur
- `status` (boolean) : Statut du serveur (true = en cours d'exécution, false = arrêté)

---

## Opérations SOAP

### 1. createServer

Crée un nouveau serveur.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:createServerRequest>
         <ser:name>Serveur-Web-1</ser:name>
         <ser:ipAddress>192.168.1.100</ser:ipAddress>
      </ser:createServerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:createServerResponse>
         <ser:server>
            <ser:id>1</ser:id>
            <ser:name>Serveur-Web-1</ser:name>
            <ser:ipAddress>192.168.1.100</ser:ipAddress>
            <ser:status>false</ser:status>
         </ser:server>
      </ser:createServerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Crée un nouveau serveur avec le nom et l'adresse IP spécifiés. Le serveur est créé avec le statut `false` (arrêté) par défaut.

---

### 2. listServers

Récupère la liste de tous les serveurs.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:listServersRequest/>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:listServersResponse>
         <ser:servers>
            <ser:id>1</ser:id>
            <ser:name>Serveur-Web-1</ser:name>
            <ser:ipAddress>192.168.1.100</ser:ipAddress>
            <ser:status>false</ser:status>
         </ser:servers>
         <ser:servers>
            <ser:id>2</ser:id>
            <ser:name>Serveur-DB-1</ser:name>
            <ser:ipAddress>192.168.1.101</ser:ipAddress>
            <ser:status>true</ser:status>
         </ser:servers>
      </ser:listServersResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Retourne la liste complète de tous les serveurs enregistrés dans le système.

---

### 3. renameServer

Renomme un serveur existant.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:renameServerRequest>
         <ser:id>1</ser:id>
         <ser:newName>Serveur-Web-Renommé</ser:newName>
      </ser:renameServerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:renameServerResponse>
         <ser:server>
            <ser:id>1</ser:id>
            <ser:name>Serveur-Web-Renommé</ser:name>
            <ser:ipAddress>192.168.1.100</ser:ipAddress>
            <ser:status>false</ser:status>
         </ser:server>
      </ser:renameServerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Renomme le serveur avec l'ID spécifié. Retourne le serveur mis à jour.

---

### 4. getServerStatus

Récupère le statut d'un serveur.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:getServerStatusRequest>
         <ser:id>1</ser:id>
      </ser:getServerStatusRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:getServerStatusResponse>
         <ser:status>true</ser:status>
      </ser:getServerStatusResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Retourne uniquement le statut (boolean) du serveur avec l'ID spécifié. `true` = en cours d'exécution, `false` = arrêté.

---

### 5. startServer

Démarre un serveur.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:startServerRequest>
         <ser:id>1</ser:id>
      </ser:startServerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:startServerResponse>
         <ser:server>
            <ser:id>1</ser:id>
            <ser:name>Serveur-Web-1</ser:name>
            <ser:ipAddress>192.168.1.100</ser:ipAddress>
            <ser:status>true</ser:status>
         </ser:server>
      </ser:startServerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Démarre le serveur avec l'ID spécifié. Le statut passe à `true` (en cours d'exécution). Retourne le serveur mis à jour.

---

### 6. stopServer

Arrête un serveur.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:stopServerRequest>
         <ser:id>1</ser:id>
      </ser:stopServerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:stopServerResponse>
         <ser:server>
            <ser:id>1</ser:id>
            <ser:name>Serveur-Web-1</ser:name>
            <ser:ipAddress>192.168.1.100</ser:ipAddress>
            <ser:status>false</ser:status>
         </ser:server>
      </ser:stopServerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Arrête le serveur avec l'ID spécifié. Le statut passe à `false` (arrêté). Retourne le serveur mis à jour.

---

### 7. deleteServer

Supprime un serveur.

**Requête** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Header/>
   <soapenv:Body>
      <ser:deleteServerRequest>
         <ser:id>1</ser:id>
      </ser:deleteServerRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Réponse** :
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:deleteServerResponse>
         <ser:deleted>true</ser:deleted>
      </ser:deleteServerResponse>
   </soapenv:Body>
</soapenv:Envelope>
```

**Description** : Supprime le serveur avec l'ID spécifié. **Important** : Le serveur doit être arrêté (status = false) avant de pouvoir être supprimé. Retourne `true` si la suppression a réussi.

---

## Codes d'Erreur SOAP

En cas d'erreur, le service retourne une faute SOAP (`SOAPFault`) avec les informations suivantes :

- **ServerNotFoundException** : Le serveur avec l'ID spécifié n'existe pas
- **InvalidServerStateException** : Tentative de supprimer un serveur en cours d'exécution

---

## Exemple d'Utilisation avec cURL

```bash
# Créer un serveur
curl -X POST http://localhost:8085/ws \
  -H "Content-Type: text/xml;charset=UTF-8" \
  -H "SOAPAction: \"\"" \
  -d '<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ser="http://supnum.com/td1/servers">
   <soapenv:Body>
      <ser:createServerRequest>
         <ser:name>Serveur-Test</ser:name>
         <ser:ipAddress>192.168.1.100</ser:ipAddress>
      </ser:createServerRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```

---

## Notes Techniques

- Le service utilise Spring WS pour l'exposition des endpoints SOAP
- Les classes Java sont générées depuis le XSD (`servers.xsd`) via JAXB
- Le service est exposé sur le port `8085`
- Le WSDL est accessible à l'URL : `http://localhost:8085/ws/servers.wsdl`

