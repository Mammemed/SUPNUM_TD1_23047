package com.supnum.td1_servers.controller;

import com.supnum.td1_servers.dto.CreateServerRequest;
import com.supnum.td1_servers.dto.RenameServerRequest;
import com.supnum.td1_servers.dto.StatusResponse;
import com.supnum.td1_servers.exception.InvalidServerStateException;
import com.supnum.td1_servers.exception.ServerNotFoundException;
import com.supnum.td1_servers.model.Server;
import com.supnum.td1_servers.service.ServerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servers")
public class ServerController {

    private final ServerService serverService;

    public ServerController(ServerService serverService) {
        this.serverService = serverService;
    }

    @PostMapping
    public ResponseEntity<Server> createServer(@RequestBody CreateServerRequest request) {
        Server server = new Server(request.getName(), request.getIpAddress());
        Server created = serverService.createServer(server);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Server>> getAllServers() {
        return ResponseEntity.ok(serverService.getAllServers());
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<Server> renameServer(@PathVariable Long id,
                                               @RequestBody RenameServerRequest request) {
        Server updated = serverService.renameServer(id, request.getNewName());
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}/status")
    public ResponseEntity<StatusResponse> getStatus(@PathVariable Long id) {
        boolean status = serverService.getServerStatus(id);
        return ResponseEntity.ok(new StatusResponse(id, status));
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<Server> startServer(@PathVariable Long id) {
        Server updated = serverService.startServer(id);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/stop")
    public ResponseEntity<Server> stopServer(@PathVariable Long id) {
        Server updated = serverService.stopServer(id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServer(@PathVariable Long id) {
        serverService.deleteServer(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ServerNotFoundException.class)
    public ResponseEntity<String> handleNotFound(ServerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidServerStateException.class)
    public ResponseEntity<String> handleInvalidState(InvalidServerStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
