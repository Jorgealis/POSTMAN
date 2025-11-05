package com.example.demo.controller;

import com.example.demo.model.RequestLog;
import com.example.demo.repository.RequestLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador para consultar los logs de solicitudes HTTP
 * Útil para auditoría y análisis de uso de la API
 */
@RestController
@RequestMapping("/api/logs")
public class RequestLogController {

    @Autowired
    private RequestLogRepository logRepository;

    // GET /api/logs - Obtener todos los logs
    @GetMapping
    public List<RequestLog> obtenerTodosLosLogs() {
        return logRepository.findAll();
    }

    // GET /api/logs/{id} - Obtener log por ID
    @GetMapping("/{id}")
    public ResponseEntity<RequestLog> obtenerLogPorId(@PathVariable Long id) {
        return logRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /api/logs/method/{method} - Filtrar por método HTTP
    @GetMapping("/method/{method}")
    public List<RequestLog> obtenerPorMetodo(@PathVariable String method) {
        return logRepository.findByMethod(method.toUpperCase());
    }

    // GET /api/logs/ip/{ip} - Filtrar por IP
    @GetMapping("/ip/{ip}")
    public List<RequestLog> obtenerPorIp(@PathVariable String ip) {
        return logRepository.findByIp(ip);
    }

    // GET /api/logs/path?search={path} - Buscar por path
    @GetMapping("/path")
    public List<RequestLog> buscarPorPath(@RequestParam String search) {
        return logRepository.findByPathContaining(search);
    }

    // DELETE /api/logs - Eliminar todos los logs (útil para limpieza)
    @DeleteMapping
    public ResponseEntity<?> eliminarTodosLosLogs() {
        long count = logRepository.count();
        logRepository.deleteAll();
        return ResponseEntity.ok(
            java.util.Map.of(
                "mensaje", "Logs eliminados exitosamente",
                "cantidad", count
            )
        );
    }

    // GET /api/logs/count - Obtener conteo de logs
    @GetMapping("/count")
    public ResponseEntity<?> contarLogs() {
        return ResponseEntity.ok(
            java.util.Map.of(
                "total", logRepository.count()
            )
        );
    }
}