package com.supnum.td1_servers.repository;

import com.supnum.td1_servers.model.Server;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServerRepository extends JpaRepository<Server, Long> {
    boolean existsByIpAddress(String ipAddress);
}
