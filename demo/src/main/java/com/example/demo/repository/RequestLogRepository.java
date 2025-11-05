package com.example.demo.repository;

import com.example.demo.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findByMethod(String method);
    List<RequestLog> findByIp(String ip);
    List<RequestLog> findByPathContaining(String path);
}