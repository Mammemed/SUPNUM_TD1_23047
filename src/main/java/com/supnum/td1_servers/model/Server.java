package com.supnum.td1_servers.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "servers")
public class Server {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "ip_address", nullable = false, unique = true)
    private String ipAddress;

    @Column(nullable = false)
    private boolean running = false;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Server() {}

    public Server(String name, String ipAddress) {
        this.name = name;
        this.ipAddress = ipAddress;
    }

    // Getters / Setters

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getIpAddress() { return ipAddress; }

    public boolean isRunning() { return running; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { this.name = name; }

    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public void setRunning(boolean running) { this.running = running; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
