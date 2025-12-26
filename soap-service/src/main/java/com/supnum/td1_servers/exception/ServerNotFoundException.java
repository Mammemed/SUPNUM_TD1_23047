package com.supnum.td1_servers.exception;

public class ServerNotFoundException extends RuntimeException {
    public ServerNotFoundException(Long id) {
        super("Server with id " + id + " not found");
    }
}
