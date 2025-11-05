package com.example.demo.controller;

import com.example.demo.model.Habilidad;
import com.example.demo.model.Personaje;
import com.example.demo.model.PersonajeHabilidad;
import com.example.demo.repository.HabilidadRepository;
import com.example.demo.repository.PersonajeHabilidadRepository;
import com.example.demo.repository.PersonajeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    @Autowired
    private PersonajeRepository personajeRepository;
    
    @Autowired
    private HabilidadRepository habilidadRepository;
    
    @Autowired
    private PersonajeHabilidadRepository personajeHabilidadRepository;

    // GET /personajes - Listar todos los personajes
    @GetMapping
    public List<Personaje> obtenerPersonajes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo) {
        
        if (nombre != null && !nombre.isBlank()) {
            return personajeRepository.findByNombreContainingIgnoreCase(nombre);
        }
        if (tipo != null && !tipo.isBlank()) {
            return personajeRepository.findByTipoContainingIgnoreCase(tipo);
        }
        return personajeRepository.findAll();
    }

    // GET /personajes/{id} - Obtener personaje por ID con sus habilidades
    @GetMapping("/{id}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable Long id) {
        return personajeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /personajes - Crear nuevo personaje
    @PostMapping
    public ResponseEntity<Personaje> crearPersonaje(@RequestBody Personaje personaje) {
        Personaje nuevoPersonaje = personajeRepository.save(personaje);
        return ResponseEntity.ok(nuevoPersonaje);
    }

    // PUT /personajes/{id} - Actualizar personaje parcialmente
    @PutMapping("/{id}")
    public ResponseEntity<Personaje> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> actualizaciones) {
        
        return personajeRepository.findById(id)
                .map(personaje -> {
                    actualizaciones.forEach((key, value) -> {
                        switch (key) {
                            case "nombre":
                                personaje.setNombre((String) value);
                                break;
                            case "tipo":
                                personaje.setTipo((String) value);
                                break;
                            case "descripcion":
                                personaje.setDescripcion((String) value);
                                break;
                            case "ataque":
                                personaje.setAtaque(((Number) value).intValue());
                                break;
                            case "defensa":
                                personaje.setDefensa(((Number) value).intValue());
                                break;
                            case "estamina":
                                personaje.setEstamina(((Number) value).intValue());
                                break;
                        }
                    });
                    return ResponseEntity.ok(personajeRepository.save(personaje));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /personajes/{id} - Eliminar personaje
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPersonaje(@PathVariable Long id) {
        if (personajeRepository.existsById(id)) {
            personajeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // POST /personajes/{id}/habilidades - Agregar habilidad a personaje
    @PostMapping("/{id}/habilidades")
    public ResponseEntity<?> agregarHabilidad(
            @PathVariable Long id,
            @RequestBody Map<String, Object> body) {
        
        Long habilidadId = ((Number) body.get("habilidadId")).longValue();
        int nivel = body.containsKey("nivel") ? ((Number) body.get("nivel")).intValue() : 1;
        
        return personajeRepository.findById(id)
                .map(personaje -> {
                    return habilidadRepository.findById(habilidadId)
                            .map(habilidad -> {
                                PersonajeHabilidad ph = new PersonajeHabilidad(personaje, habilidad, nivel);
                                personajeHabilidadRepository.save(ph);
                                return ResponseEntity.ok(Map.of(
                                    "mensaje", "Habilidad agregada exitosamente",
                                    "personajeHabilidad", ph
                                ));
                            })
                            .orElseGet(() -> ResponseEntity.badRequest()
                                .body(Map.of("error", "Habilidad no encontrada")));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET /personajes/{id}/habilidades - Listar habilidades de un personaje
    @GetMapping("/{id}/habilidades")
    public ResponseEntity<List<PersonajeHabilidad>> obtenerHabilidades(@PathVariable Long id) {
        if (!personajeRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<PersonajeHabilidad> habilidades = personajeHabilidadRepository.findByPersonajeId(id);
        return ResponseEntity.ok(habilidades);
    }

    @GetMapping("/{idPersonaje}/habilidades/{idHabilidad}")
public ResponseEntity<?> obtenerHabilidadEspecifica(
        @PathVariable Long idPersonaje,
        @PathVariable Long idHabilidad) {
    
    return personajeHabilidadRepository.findByPersonajeIdAndHabilidadId(idPersonaje, idHabilidad)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
}

// PUT /personajes/{idPersonaje}/habilidades/{idHabilidad} - Actualizar nivel
@PutMapping("/{idPersonaje}/habilidades/{idHabilidad}")
public ResponseEntity<?> actualizarNivelHabilidad(
        @PathVariable Long idPersonaje,
        @PathVariable Long idHabilidad,
        @RequestBody Map<String, Object> body) {
    
    int nuevoNivel = ((Number) body.get("nivel")).intValue();
    
    return personajeHabilidadRepository.findByPersonajeIdAndHabilidadId(idPersonaje, idHabilidad)
            .map(ph -> {
                ph.setNivel(nuevoNivel);
                personajeHabilidadRepository.save(ph);
                return ResponseEntity.ok(Map.of(
                    "mensaje", "Nivel de habilidad actualizado exitosamente",
                    "personajeHabilidad", ph
                ));
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
}

    // DELETE /personajes/{idPersonaje}/habilidades/{idHabilidad} - Quitar habilidad
    @DeleteMapping("/{idPersonaje}/habilidades/{idHabilidad}")
    public ResponseEntity<?> quitarHabilidad(
            @PathVariable Long idPersonaje,
            @PathVariable Long idHabilidad) {
        
        if (!personajeRepository.existsById(idPersonaje)) {
            return ResponseEntity.notFound().build();
        }
        
        return personajeHabilidadRepository.findByPersonajeIdAndHabilidadId(idPersonaje, idHabilidad)
                .map(ph -> {
                    personajeHabilidadRepository.delete(ph);
                    return ResponseEntity.ok(Map.of("mensaje", "Habilidad removida exitosamente"));
                })
                .orElseGet(() -> ResponseEntity.badRequest()
                    .body(Map.of("error", "Relación personaje-habilidad no encontrada")));
    }
}