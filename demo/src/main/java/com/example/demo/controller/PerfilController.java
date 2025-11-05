// ===== PerfilController.java =====
package com.example.demo.controller;

import com.example.demo.model.Perfil;
import com.example.demo.model.Usuario;
import com.example.demo.repository.PerfilRepository;
import com.example.demo.repository.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/perfiles")
public class PerfilController {

    @Autowired
    private PerfilRepository perfilRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    // GET /perfiles - Listar todos los perfiles
    @GetMapping
    public List<Perfil> obtenerPerfiles() {
        return perfilRepository.findAll();
    }

    // GET /perfiles/{id} - Obtener perfil por ID
    @GetMapping("/{id}")
    public ResponseEntity<Perfil> obtenerPorId(@PathVariable Long id) {
        return perfilRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST /perfiles - Crear nuevo perfil asociado a un usuario
    @PostMapping
    public ResponseEntity<?> crearPerfil(@RequestBody Map<String, Object> body) {
        Long usuarioId = ((Number) body.get("usuarioId")).longValue();
        String biografia = (String) body.get("biografia");
        String avatar = (String) body.get("avatar");
        
        return usuarioRepository.findById(usuarioId)
                .map(usuario -> {
                    if (usuario.getPerfil() != null) {
                        return ResponseEntity.badRequest()
                                .body(Map.of("error", "El usuario ya tiene un perfil"));
                    }
                    
                    Perfil perfil = new Perfil(biografia, avatar);
                    usuario.setPerfil(perfil);
                    usuarioRepository.save(usuario);
                    
                    return ResponseEntity.ok(perfil);
                })
                .orElseGet(() -> ResponseEntity.badRequest()
                        .body(Map.of("error", "Usuario no encontrado")));
    }

    // PUT /perfiles/{id} - Actualizar perfil
    @PutMapping("/{id}")
    public ResponseEntity<Perfil> actualizarPerfil(
            @PathVariable Long id,
            @RequestBody Map<String, Object> actualizaciones) {
        
        return perfilRepository.findById(id)
                .map(perfil -> {
                    actualizaciones.forEach((key, value) -> {
                        switch (key) {
                            case "biografia":
                                perfil.setBiografia((String) value);
                                break;
                            case "avatar":
                                perfil.setAvatar((String) value);
                                break;
                        }
                    });
                    return ResponseEntity.ok(perfilRepository.save(perfil));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // DELETE /perfiles/{id} - Eliminar perfil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPerfil(@PathVariable Long id) {
        if (perfilRepository.existsById(id)) {
            perfilRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
