package com.supnum.td1_servers.service;

import com.supnum.td1_servers.exception.InvalidServerStateException;
import com.supnum.td1_servers.exception.ServerNotFoundException;
import com.supnum.td1_servers.model.Server;
import com.supnum.td1_servers.repository.ServerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public Server createServer(Server server) {
        if (serverRepository.existsByIpAddress(server.getIpAddress())) {
            throw new InvalidServerStateException("IP address already used by another server");
        }
        return serverRepository.save(server);
    }

    @Override
    public List<Server> getAllServers() {
        return serverRepository.findAll();
    }

    @Override
    public Server renameServer(Long id, String newName) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(id));
        server.setName(newName);
        return serverRepository.save(server);
    }

    @Override
    public boolean getServerStatus(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(id));
        return server.isRunning();
    }

    @Override
    public Server startServer(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(id));
        server.setRunning(true);
        return serverRepository.save(server);
    }

    @Override
    public Server stopServer(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(id));
        server.setRunning(false);
        return serverRepository.save(server);
    }

    @Override
    public void deleteServer(Long id) {
        Server server = serverRepository.findById(id)
                .orElseThrow(() -> new ServerNotFoundException(id));

        if (server.isRunning()) {
            throw new InvalidServerStateException("Cannot delete a running server. Stop it first.");
        }

        serverRepository.delete(server);
    }
}
