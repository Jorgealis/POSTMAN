// ===== HabilidadRepository.java =====
package com.example.demo.repository;

import com.example.demo.model.Habilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface HabilidadRepository extends JpaRepository<Habilidad, Long> {
    List<Habilidad> findByNombreContainingIgnoreCase(String nombre);
    List<Habilidad> findByOrderByIncrementoEstaminaAsc();
    List<Habilidad> findByOrderByIncrementoEstaminaDesc();
}
