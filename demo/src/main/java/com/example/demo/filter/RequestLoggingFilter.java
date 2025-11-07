package com.example.demo.filter;

import com.example.demo.model.RequestLog;
import com.example.demo.repository.RequestLogRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filtro de Pre-procesamiento
 * Registra todas las solicitudes HTTP que llegan a la API
 * antes de que sean procesadas por los controladores.
 */
@Component
public class RequestLoggingFilter implements Filter {

    @Autowired
    private RequestLogRepository logRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        // Convertir a HttpServletRequest para acceder a métodos HTTP específicos
        HttpServletRequest req = (HttpServletRequest) request;
        
        // Crear el log de la solicitud
        RequestLog log = new RequestLog();
        log.setMethod(req.getMethod());
        log.setPath(req.getRequestURI());
        log.setIp(req.getRemoteAddr());
        log.setTimestamp(LocalDateTime.now());
        
        // Guardar en la base de datos
        try {
            logRepository.save(log);
            System.out.println("✓ [LOG] " + req.getMethod() + " " + req.getRequestURI() + " desde " + req.getRemoteAddr());
        } catch (Exception e) {
            System.err.println("✗ [ERROR] No se pudo guardar el log: " + e.getMessage());
        }
        
        // Continuar con la cadena de filtros y controladores
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🚀 RequestLoggingFilter inicializado - Monitoreando todas las solicitudes");
    }

    @Override
    public void destroy() {
        System.out.println("🛑 RequestLoggingFilter destruido");
    }
}