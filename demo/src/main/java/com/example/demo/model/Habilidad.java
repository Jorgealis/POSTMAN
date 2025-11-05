package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "habilidades")
public class Habilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private int incrementoAtaque;
    
    @Column(nullable = false)
    private int incrementoDefensa;
    
    @Column(nullable = false)
    private int incrementoEstamina;
    
    // Relación Many-to-Many con Personaje (a través de tabla intermedia)
    @OneToMany(mappedBy = "habilidad", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Ignorar completamente esta lista para evitar recursión
    private List<PersonajeHabilidad> personajeHabilidades = new ArrayList<>();
    
    // Constructores
    public Habilidad() {
    }
    
    public Habilidad(String nombre, String descripcion, int incrementoAtaque, int incrementoDefensa, int incrementoEstamina) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.incrementoAtaque = incrementoAtaque;
        this.incrementoDefensa = incrementoDefensa;
        this.incrementoEstamina = incrementoEstamina;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getIncrementoAtaque() {
        return incrementoAtaque;
    }
    
    public void setIncrementoAtaque(int incrementoAtaque) {
        this.incrementoAtaque = incrementoAtaque;
    }
    
    public int getIncrementoDefensa() {
        return incrementoDefensa;
    }
    
    public void setIncrementoDefensa(int incrementoDefensa) {
        this.incrementoDefensa = incrementoDefensa;
    }
    
    public int getIncrementoEstamina() {
        return incrementoEstamina;
    }
    
    public void setIncrementoEstamina(int incrementoEstamina) {
        this.incrementoEstamina = incrementoEstamina;
    }
    
    public List<PersonajeHabilidad> getPersonajeHabilidades() {
        return personajeHabilidades;
    }
    
    public void setPersonajeHabilidades(List<PersonajeHabilidad> personajeHabilidades) {
        this.personajeHabilidades = personajeHabilidades;
    }
}