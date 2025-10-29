package com.example.demo.config;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {
    
    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PersonajeRepository personajeRepository,
            HabilidadRepository habilidadRepository,
            PersonajeHabilidadRepository personajeHabilidadRepository) {
        
        return args -> {
            // Verificar si ya hay datos
            if (usuarioRepository.count() > 0) {
                System.out.println("Base de datos ya contiene datos. Omitiendo seeding.");
                return;
            }
            
            System.out.println("Iniciando carga de datos...");
            
            // ===== CREAR HABILIDADES =====
            Habilidad h1 = new Habilidad("Espadazo", "Un ataque poderoso con la espada.", 10, 0, -5);
            Habilidad h2 = new Habilidad("Escudo de Hierro", "Aumenta la defensa del guerrero.", 0, 15, -3);
            Habilidad h3 = new Habilidad("Agua de la vida", "Recupera PV al instante", 0, 0, 20);
            Habilidad h4 = new Habilidad("Bola de fuego", "Lanza una bola de fuego al enemigo", 15, 0, -10);
            Habilidad h5 = new Habilidad("Muerte sin retorno", "Ataque letal que acaba con el enemigo instantáneamente", 100, 0, -50);
            
            habilidadRepository.save(h1);
            habilidadRepository.save(h2);
            habilidadRepository.save(h3);
            habilidadRepository.save(h4);
            habilidadRepository.save(h5);
            
            System.out.println("✓ 5 Habilidades creadas");
            
            // ===== CREAR USUARIOS Y PERFILES =====
            // Usuario 1
            Usuario u1 = new Usuario("dragon_slayer", "pass123", "dragon@mail.com");
            Perfil p1 = new Perfil("Guerrero experimentado en busca de aventuras", "https://example.com/avatar1.jpg");
            u1.setPerfil(p1);
            usuarioRepository.save(u1);
            
            // Usuario 2
            Usuario u2 = new Usuario("magic_master", "pass456", "magic@mail.com");
            Perfil p2 = new Perfil("Mago estudiante de las artes arcanas", "https://example.com/avatar2.jpg");
            u2.setPerfil(p2);
            usuarioRepository.save(u2);
            
            // Usuario 3
            Usuario u3 = new Usuario("healer_life", "pass789", "healer@mail.com");
            Perfil p3 = new Perfil("Sanador dedicado a proteger a los débiles", "https://example.com/avatar3.jpg");
            u3.setPerfil(p3);
            usuarioRepository.save(u3);
            
            System.out.println("✓ 3 Usuarios y Perfiles creados");
            
            // ===== CREAR PERSONAJES =====
            // Personajes del Usuario 1
            Personaje per1 = new Personaje("Gagh-Ar", "guerrero", "Un valiente luchador con gran fuerza física.", 80, 70, 60);
            per1.setPerfil(p1);
            personajeRepository.save(per1);
            
            Personaje per2 = new Personaje("Elyra", "maga", "Hechicera con gran dominio de la energía mágica.", 65, 40, 90);
            per2.setPerfil(p1);
            personajeRepository.save(per2);
            
            // Personajes del Usuario 2
            Personaje per3 = new Personaje("Thalor", "sanador", "Un sabio sanador que protege a sus aliados.", 50, 60, 100);
            per3.setPerfil(p2);
            personajeRepository.save(per3);
            
            Personaje per4 = new Personaje("Zarak", "mago", "Un mago muy curioso", 70, 50, 80);
            per4.setPerfil(p2);
            personajeRepository.save(per4);
            
            // Personajes del Usuario 3
            Personaje per5 = new Personaje("Krell", "asesino", "Un asesino sigiloso y letal", 90, 40, 70);
            per5.setPerfil(p3);
            personajeRepository.save(per5);
            
            System.out.println("✓ 5 Personajes creados");
            
            // ===== ASIGNAR HABILIDADES A PERSONAJES CON NIVEL =====
            // Gagh-Ar tiene Espadazo (nivel 3) y Escudo de Hierro (nivel 2)
            PersonajeHabilidad ph1 = new PersonajeHabilidad(per1, h1, 3);
            PersonajeHabilidad ph2 = new PersonajeHabilidad(per1, h2, 2);
            personajeHabilidadRepository.save(ph1);
            personajeHabilidadRepository.save(ph2);
            
            // Elyra tiene Escudo de Hierro (nivel 1)
            PersonajeHabilidad ph3 = new PersonajeHabilidad(per2, h2, 1);
            personajeHabilidadRepository.save(ph3);
            
            // Thalor tiene Agua de la vida (nivel 5)
            PersonajeHabilidad ph4 = new PersonajeHabilidad(per3, h3, 5);
            personajeHabilidadRepository.save(ph4);
            
            // Zarak tiene Bola de fuego (nivel 4)
            PersonajeHabilidad ph5 = new PersonajeHabilidad(per4, h4, 4);
            personajeHabilidadRepository.save(ph5);
            
            // Krell tiene Muerte sin retorno (nivel 10)
            PersonajeHabilidad ph6 = new PersonajeHabilidad(per5, h5, 10);
            personajeHabilidadRepository.save(ph6);
            
            System.out.println("✓ Habilidades asignadas a personajes");
            System.out.println("===== Carga de datos completada =====");
        };
    }
}