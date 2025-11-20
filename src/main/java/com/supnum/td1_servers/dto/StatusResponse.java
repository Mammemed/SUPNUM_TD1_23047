package com.supnum.td1_servers.dto;

public class StatusResponse {
    private Long id;
    private boolean running;

    public StatusResponse(Long id, boolean running) {
        this.id = id;
        this.running = running;
    }

    public Long getId() { return id; }

    public boolean isRunning() { return running; }
}
