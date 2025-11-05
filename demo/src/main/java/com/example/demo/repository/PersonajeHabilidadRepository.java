// ===== PersonajeHabilidadRepository.java =====
package com.example.demo.repository;

import com.example.demo.model.PersonajeHabilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PersonajeHabilidadRepository extends JpaRepository<PersonajeHabilidad, Long> {
    List<PersonajeHabilidad> findByPersonajeId(Long personajeId);
    Optional<PersonajeHabilidad> findByPersonajeIdAndHabilidadId(Long personajeId, Long habilidadId);
    void deleteByPersonajeIdAndHabilidadId(Long personajeId, Long habilidadId);
}