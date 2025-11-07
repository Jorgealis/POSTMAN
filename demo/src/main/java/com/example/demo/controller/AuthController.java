package com.example.demo.controller;

import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador para manejar autenticación y registro de usuarios
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    /**
     * POST /api/auth/login - Iniciar sesión
     * Body: { "username": "admin", "password": "admin123" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        try {
            // Autenticar al usuario
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Generar token JWT
            String jwt = jwtUtils.generateJwtToken(authentication);
            
            // Obtener información del usuario
            Usuario usuario = usuarioRepository.findByNombreUsuario(username)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("username", usuario.getNombreUsuario());
            response.put("email", usuario.getCorreo());
            response.put("role", usuario.getRole());
            response.put("id", usuario.getId());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Credenciales inválidas");
            errorResponse.put("mensaje", "Usuario o contraseña incorrectos");
            return ResponseEntity.status(401).body(errorResponse);
        }
    }
    
    /**
     * POST /api/auth/register - Registrar nuevo usuario
     * Body: { "username": "nuevo", "password": "pass123", "email": "nuevo@mail.com" }
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Map<String, String> signUpRequest) {
        String username = signUpRequest.get("username");
        String email = signUpRequest.get("email");
        String password = signUpRequest.get("password");
        String role = signUpRequest.getOrDefault("role", "USER");
        
        // Validar que no exista el usuario
        if (usuarioRepository.existsByNombreUsuario(username)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El nombre de usuario ya existe"));
        }
        
        if (usuarioRepository.existsByCorreo(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "El correo ya está registrado"));
        }
        
        // Crear nuevo usuario con contraseña encriptada
        Usuario nuevoUsuario = new Usuario(
                username,
                passwordEncoder.encode(password),
                email,
                role
        );
        
        usuarioRepository.save(nuevoUsuario);
        
        Map<String, Object> response = new HashMap<>();
        response.put("mensaje", "Usuario registrado exitosamente");
        response.put("username", nuevoUsuario.getNombreUsuario());
        response.put("email", nuevoUsuario.getCorreo());
        response.put("role", nuevoUsuario.getRole());
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * GET /api/auth/me - Obtener información del usuario autenticado
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        return usuarioRepository.findByNombreUsuario(username)
                .map(usuario -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("id", usuario.getId());
                    response.put("username", usuario.getNombreUsuario());
                    response.put("email", usuario.getCorreo());
                    response.put("role", usuario.getRole());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}