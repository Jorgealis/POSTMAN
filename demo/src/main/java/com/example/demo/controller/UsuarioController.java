
// ===== UsuarioController.java =====
package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.model.Personaje;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.repository.PersonajeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PersonajeRepository personajeRepository;

    // GET /usuarios - Listar todos los usuarios
    @GetMapping
    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    // GET /usuarios/{id} - Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /usuarios - Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario) {
        // Validar que no exista el nombre de usuario o correo
        if (usuarioRepository.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre de usuario ya existe"));
        }
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El correo ya está registrado"));
        }
        
        Usuario nuevoUsuario = usuarioRepository.save(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // PUT /usuarios/{id} - Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUsuario(
            @PathVariable Long id,
            @RequestBody Map<String, Object> actualizaciones) {
        
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    actualizaciones.forEach((key, value) -> {
                        switch (key) {
                            case "nombreUsuario":
                                usuario.setNombreUsuario((String) value);
                                break;
                            case "contrasena":
                                usuario.setContrasena((String) value);
                                break;
                            case "correo":
                                usuario.setCorreo((String) value);
                                break;
                        }
                    });
                    return ResponseEntity.ok(usuarioRepository.save(usuario));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /usuarios/{id} - Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /usuarios/{id}/personajes - Obtener personajes de un usuario
    @GetMapping("/{id}/personajes")
    public ResponseEntity<List<Personaje>> obtenerPersonajesDeUsuario(@PathVariable Long id) {
        return usuarioRepository.findById(id)
                .map(usuario -> {
                    if (usuario.getPerfil() != null) {
                        List<Personaje> personajes = personajeRepository
                                .findByPerfilId(usuario.getPerfil().getId());
                        return ResponseEntity.ok(personajes);
                    }
                    return ResponseEntity.ok(List.<Personaje>of());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}