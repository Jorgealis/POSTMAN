package com.example.demo.controller;

import com.example.demo.model.Habilidad;
import com.example.demo.repository.HabilidadRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/habilidades")
public class HabilidadController {

    @Autowired
    private HabilidadRepository habilidadRepository;

    // GET /habilidades - Listar todas las habilidades
    @GetMapping
    public List<Habilidad> obtenerHabilidades(
            @RequestParam(required = false) String nombre) {
        
        if (nombre != null && !nombre.isBlank()) {
            return habilidadRepository.findByNombreContainingIgnoreCase(nombre);
        }
        return habilidadRepository.findAll();
    }

    // GET /habilidades/{id} - Obtener habilidad por ID
    @GetMapping("/{id}")
    public ResponseEntity<Habilidad> obtenerPorId(@PathVariable Long id) {
        return habilidadRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /habilidades/ordenar - Ordenar por incremento de estamina
    @GetMapping("/ordenar")
    public List<Habilidad> ordenarPorEstamina(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "asc") String order) {
        
        if ("incrementoEstamina".equalsIgnoreCase(sort)) {
            if ("asc".equalsIgnoreCase(order)) {
                return habilidadRepository.findByOrderByIncrementoEstaminaAsc();
            } else {
                return habilidadRepository.findByOrderByIncrementoEstaminaDesc();
            }
        }
        return habilidadRepository.findAll();
    }

    // POST /habilidades - Crear nueva habilidad
    @PostMapping
    public ResponseEntity<Habilidad> crearHabilidad(@RequestBody Habilidad habilidad) {
        Habilidad nuevaHabilidad = habilidadRepository.save(habilidad);
        return ResponseEntity.ok(nuevaHabilidad);
    }

    // PUT /habilidades/{id} - Actualizar habilidad parcialmente
    @PutMapping("/{id}")
    public ResponseEntity<Habilidad> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> actualizaciones) {
        
        return habilidadRepository.findById(id)
                .map(habilidad -> {
                    actualizaciones.forEach((key, value) -> {
                        switch (key) {
                            case "nombre":
                                habilidad.setNombre((String) value);
                                break;
                            case "descripcion":
                                habilidad.setDescripcion((String) value);
                                break;
                            case "incrementoAtaque":
                                habilidad.setIncrementoAtaque(((Number) value).intValue());
                                break;
                            case "incrementoDefensa":
                                habilidad.setIncrementoDefensa(((Number) value).intValue());
                                break;
                            case "incrementoEstamina":
                                habilidad.setIncrementoEstamina(((Number) value).intValue());
                                break;
                        }
                    });
                    return ResponseEntity.ok(habilidadRepository.save(habilidad));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /habilidades/{id} - Eliminar habilidad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarHabilidad(@PathVariable Long id) {
        if (habilidadRepository.existsById(id)) {
            habilidadRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}