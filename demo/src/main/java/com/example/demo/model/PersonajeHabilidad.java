package com.example.demo.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "personaje_habilidad")
public class PersonajeHabilidad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "personaje_id")
    @JsonIgnore // Evita la recursión infinita
    private Personaje personaje;
    
    @ManyToOne
    @JoinColumn(name = "habilidad_id")
    @JsonIgnoreProperties("personajeHabilidades") // Ignora la lista de personajeHabilidades en Habilidad
    private Habilidad habilidad;
    
    @Column(nullable = false)
    private int nivel;
    
    // Constructores
    public PersonajeHabilidad() {
    }
    
    public PersonajeHabilidad(Personaje personaje, Habilidad habilidad, int nivel) {
        this.personaje = personaje;
        this.habilidad = habilidad;
        this.nivel = nivel;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Personaje getPersonaje() {
        return personaje;
    }
    
    public void setPersonaje(Personaje personaje) {
        this.personaje = personaje;
    }
    
    public Habilidad getHabilidad() {
        return habilidad;
    }
    
    public void setHabilidad(Habilidad habilidad) {
        this.habilidad = habilidad;
    }
    
    public int getNivel() {
        return nivel;
    }
    
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}