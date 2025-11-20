package com.supnum.td1_servers.exception;

public class InvalidServerStateException extends RuntimeException {
    public InvalidServerStateException(String message) {
        super(message);
    }
}
