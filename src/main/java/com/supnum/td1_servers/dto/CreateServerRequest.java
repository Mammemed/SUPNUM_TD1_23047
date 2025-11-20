package com.supnum.td1_servers.dto;

public class CreateServerRequest {
    private String name;
    private String ipAddress;

    public String getName() { return name; }

    public String getIpAddress() { return ipAddress; }

    public void setName(String name) { this.name = name; }

    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
