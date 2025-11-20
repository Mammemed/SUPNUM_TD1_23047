package com.supnum.td1_servers.service;

import com.supnum.td1_servers.model.Server;

import java.util.List;

public interface ServerService {
    Server createServer(Server server);
    List<Server> getAllServers();
    Server renameServer(Long id, String newName);
    boolean getServerStatus(Long id);
    Server startServer(Long id);
    Server stopServer(Long id);
    void deleteServer(Long id);
}
