package com.example.demo.controller;

import java.util.Iterator;

import com.example.demo.repository.Datosjuego;
import com.example.demo.model.Habilidad;
import com.example.demo.model.Personaje;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.apache.catalina.connector.Response;
@RestController
@RequestMapping("/api/personajes")
public class PersonajeController {

    @GetMapping
    public List<Personaje> obtenerPersonajes(@RequestParam(required = false) String nombre,
            @RequestParam(required = false) String tipo) {
        if (nombre != null && !nombre.isBlank()) {
            String q = nombre.toLowerCase();
            return Datosjuego.PERSONAJES.stream()
                    .filter(p -> p.getNombre() != null && p.getNombre().toLowerCase().contains(q))
                    .collect(Collectors.toList());
        }
        if (tipo != null && !tipo.isBlank()) {
            String t = tipo.toLowerCase();
            return Datosjuego.PERSONAJES.stream()
                    .filter(p -> p.getTipo() != null && p.getTipo().toLowerCase().contains(t))
                    .collect(Collectors.toList());
        }
        return Datosjuego.PERSONAJES;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Personaje> obtenerPorId(@PathVariable int id) {
        return Datosjuego.PERSONAJES.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // *Añadir este POST para enseñarle a mi programita como hacer un POST porque no
    // sabe */
    @PostMapping
    public ResponseEntity<Personaje> crearPersonaje(@RequestBody Personaje personaje) {
        Datosjuego.PERSONAJES.add(personaje);
        return ResponseEntity.ok(personaje);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Personaje> actualizarParcial(@PathVariable int id,
            @RequestBody Map<String, Object> actualizaciones) {
        for (Personaje personaje : Datosjuego.PERSONAJES) {
            if (personaje.getId() == id) {
                for (Map.Entry<String, Object> entry : actualizaciones.entrySet()) {
                    switch (entry.getKey()) {
                        case "nombre":
                            personaje.setNombre((String) entry.getValue());
                            break;
                        case "tipo":
                            personaje.setTipo((String) entry.getValue());
                            break;
                        case "descripcion":
                            personaje.setDescripcion((String) entry.getValue());
                            break;
                        case "ataque":
                            personaje.setAtaque(((Number) entry.getValue()).intValue());
                            break;
                        case "defensa":
                            personaje.setDefensa(((Number) entry.getValue()).intValue());
                            break;
                        case "estamina":
                            personaje.setEstamina(((Number) entry.getValue()).intValue());
                            break;
                        case "habilidades":
                            personaje.setHabilidades((List<Integer>) entry.getValue());
                            break;
                        // Puedes agregar más campos aquí si lo necesitas
                    }
                }
                return ResponseEntity.ok(personaje);
            }
        }
        return ResponseEntity.notFound().build();
    }

    //ahora vamos con un DELETE AQUI EN POSTMAN NO ES NECESARIO MANDAR NADA CON UNA URL Y EL ID DE LO QUE QUEREMOS ELIMINAR ES SUFICIENTE BIEN?

    @DeleteMapping("/{id}")
    public ResponseEntity<Personaje> eliminarPersonaje(@PathVariable int id){
        Iterator<Personaje> iterator=Datosjuego.PERSONAJES.iterator();
        while (iterator.hasNext()){
            Personaje personaje=iterator.next();
            if (personaje.getId()==id){
                iterator.remove();//aqui quitamos nuestro personaje de la lista
                return ResponseEntity.ok().build();//devolvemos una respuesta exitosa sin contenido
            }
        }
        return ResponseEntity.notFound().build();//si no encontramos el personaje devolvemos 404
    }

}
