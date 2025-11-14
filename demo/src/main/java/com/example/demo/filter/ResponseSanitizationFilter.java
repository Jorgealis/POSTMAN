package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Filtro de Post-procesamiento
 * Sanitiza las respuestas JSON eliminando campos que terminen en "_id"
 * para ocultar detalles internos de la base de datos.
 */
@Component
@Order(2)
public class ResponseSanitizationFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // NO aplicar sanitización a rutas de autenticación
        String path = httpRequest.getRequestURI();
        if (path.startsWith("/api/auth/") || path.startsWith("/h2-console")) {
            chain.doFilter(request, response);
            return;
        }
        
        ContentCachingResponseWrapper wrappedResponse = 
            new ContentCachingResponseWrapper(httpResponse);
        
        // Ejecutar la cadena de filtros y el controlador
        chain.doFilter(request, wrappedResponse);
        
        // Obtener el contenido de la respuesta
        byte[] responseArray = wrappedResponse.getContentAsByteArray();
        String responseBody = new String(responseArray, StandardCharsets.UTF_8);
        
        // Solo procesar si hay contenido y es JSON
        if (responseBody != null && !responseBody.isEmpty() && 
            wrappedResponse.getContentType() != null && 
            wrappedResponse.getContentType().contains("application/json")) {
            
            try {
                String sanitized = sanitizeJson(responseBody);
                
                // Escribir la respuesta sanitizada
                response.setContentLength(sanitized.getBytes(StandardCharsets.UTF_8).length);
                response.getOutputStream().write(sanitized.getBytes(StandardCharsets.UTF_8));
                
                System.out.println("🔒 [SANITIZE] Respuesta sanitizada - campos '_id' eliminados");
            } catch (Exception e) {
                // Si hay error al parsear JSON, devolver la respuesta original
                System.err.println("⚠️ [SANITIZE] No se pudo sanitizar: " + e.getMessage());
                wrappedResponse.copyBodyToResponse();
            }
        } else {
            // Si no es JSON, devolver la respuesta original
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * Sanitiza el JSON eliminando campos que terminen en "_id"
     */
    private String sanitizeJson(String json) {
        if (json.trim().startsWith("[")) {
            // Es un array JSON
            JSONArray jsonArray = new JSONArray(json);
            JSONArray sanitizedArray = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                Object item = jsonArray.get(i);
                if (item instanceof JSONObject) {
                    sanitizedArray.put(removeIdAttributes((JSONObject) item));
                } else {
                    sanitizedArray.put(item);
                }
            }
            return sanitizedArray.toString();
        } else if (json.trim().startsWith("{")) {
            // Es un objeto JSON
            JSONObject jsonObject = new JSONObject(json);
            removeIdAttributes(jsonObject);
            return jsonObject.toString();
        }
        return json;
    }

    /**
     * Elimina recursivamente todos los atributos que terminen en "_id"
     */
    private JSONObject removeIdAttributes(JSONObject jsonObject) {
        Iterator<String> keys = jsonObject.keys();
        
        // Crear una lista de claves a eliminar
        java.util.List<String> keysToRemove = new java.util.ArrayList<>();
        
        while (keys.hasNext()) {
            String key = keys.next();
            
            // Marcar para eliminación si termina en "_id"
            if (key.endsWith("_id")) {
                keysToRemove.add(key);
            } 
            // Procesar recursivamente si es un objeto anidado
            else if (jsonObject.get(key) instanceof JSONObject) {
                removeIdAttributes(jsonObject.getJSONObject(key));
            }
            // Procesar arrays anidados
            else if (jsonObject.get(key) instanceof JSONArray) {
                JSONArray array = jsonObject.getJSONArray(key);
                for (int i = 0; i < array.length(); i++) {
                    if (array.get(i) instanceof JSONObject) {
                        removeIdAttributes(array.getJSONObject(i));
                    }
                }
            }
        }
        
        // Eliminar las claves marcadas
        for (String key : keysToRemove) {
            jsonObject.remove(key);
        }
        
        return jsonObject;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("🚀 ResponseSanitizationFilter inicializado - Sanitizando respuestas");
    }

    @Override
    public void destroy() {
        System.out.println("🛑 ResponseSanitizationFilter destruido");
    }
}