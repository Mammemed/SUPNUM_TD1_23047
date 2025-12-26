package com.supnum.td1_servers.soap;

import com.supnum.td1_servers.service.ServerService;
import com.supnum.td1.servers.CreateServerRequest;
import com.supnum.td1.servers.CreateServerResponse;
import com.supnum.td1.servers.DeleteServerRequest;
import com.supnum.td1.servers.DeleteServerResponse;
import com.supnum.td1.servers.GetServerStatusRequest;
import com.supnum.td1.servers.GetServerStatusResponse;
import com.supnum.td1.servers.ListServersRequest;
import com.supnum.td1.servers.ListServersResponse;
import com.supnum.td1.servers.RenameServerRequest;
import com.supnum.td1.servers.RenameServerResponse;
import com.supnum.td1.servers.Server;
import com.supnum.td1.servers.StartServerRequest;
import com.supnum.td1.servers.StartServerResponse;
import com.supnum.td1.servers.StopServerRequest;
import com.supnum.td1.servers.StopServerResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class ServerEndpoint {

    private static final String NAMESPACE_URI = "http://supnum.com/td1/servers";

    private final ServerService serverService;

    public ServerEndpoint(ServerService serverService) {
        this.serverService = serverService;
    }

    // ========================= CREATE SERVER =========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createServerRequest")
    @ResponsePayload
    public CreateServerResponse createServer(@RequestPayload CreateServerRequest request) {
        // Entité JPA
        com.supnum.td1_servers.model.Server entity = new com.supnum.td1_servers.model.Server();
        entity.setName(request.getName());
        entity.setIpAddress(request.getIpAddress());
        entity.setRunning(false); // par défaut éteint

        entity = serverService.createServer(entity);

        CreateServerResponse response = new CreateServerResponse();
        response.setServer(toSoapServer(entity));
        return response;
    }

    // ========================= LIST SERVERS ==========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "listServersRequest")
    @ResponsePayload
    public ListServersResponse listServers(@RequestPayload ListServersRequest request) {
        List<com.supnum.td1_servers.model.Server> entities = serverService.getAllServers();

        ListServersResponse response = new ListServersResponse();
        for (com.supnum.td1_servers.model.Server entity : entities) {
            response.getServers().add(toSoapServer(entity));
        }
        return response;
    }

    // ========================= RENAME SERVER =========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "renameServerRequest")
    @ResponsePayload
    public RenameServerResponse renameServer(@RequestPayload RenameServerRequest request) {
        com.supnum.td1_servers.model.Server entity =
                serverService.renameServer(request.getId(), request.getNewName());

        RenameServerResponse response = new RenameServerResponse();
        response.setServer(toSoapServer(entity));
        return response;
    }

    // ======================= GET SERVER STATUS =======================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getServerStatusRequest")
    @ResponsePayload
    public GetServerStatusResponse getServerStatus(@RequestPayload GetServerStatusRequest request) {
        boolean status = serverService.getServerStatus(request.getId());

        GetServerStatusResponse response = new GetServerStatusResponse();
        response.setStatus(status);
        return response;
    }

    // ========================== START SERVER =========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "startServerRequest")
    @ResponsePayload
    public StartServerResponse startServer(@RequestPayload StartServerRequest request) {
        com.supnum.td1_servers.model.Server entity = serverService.startServer(request.getId());

        StartServerResponse response = new StartServerResponse();
        response.setServer(toSoapServer(entity));
        return response;
    }

    // ========================== STOP SERVER ==========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "stopServerRequest")
    @ResponsePayload
    public StopServerResponse stopServer(@RequestPayload StopServerRequest request) {
        com.supnum.td1_servers.model.Server entity = serverService.stopServer(request.getId());

        StopServerResponse response = new StopServerResponse();
        response.setServer(toSoapServer(entity));
        return response;
    }

    // ========================= DELETE SERVER =========================

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteServerRequest")
    @ResponsePayload
    public DeleteServerResponse deleteServer(@RequestPayload DeleteServerRequest request) {
        serverService.deleteServer(request.getId()); // lève une exception si running

        DeleteServerResponse response = new DeleteServerResponse();
        response.setDeleted(true);
        return response;
    }

    // ====================== MAPPING ENTITY -> SOAP ===================

    private Server toSoapServer(com.supnum.td1_servers.model.Server entity) {
        Server soap = new Server();
        soap.setId(entity.getId());
        soap.setName(entity.getName());
        soap.setIpAddress(entity.getIpAddress());
        soap.setStatus(entity.isRunning());
        return soap;
    }
}
