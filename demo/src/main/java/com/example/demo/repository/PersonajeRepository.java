// ===== PersonajeRepository.java =====
package com.example.demo.repository;

import com.example.demo.model.Personaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PersonajeRepository extends JpaRepository<Personaje, Long> {
    List<Personaje> findByNombreContainingIgnoreCase(String nombre);
    List<Personaje> findByTipoContainingIgnoreCase(String tipo);
    List<Personaje> findByPerfilId(Long perfilId);
}