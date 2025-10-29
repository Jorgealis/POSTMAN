package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "personajes")
public class Personaje {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 50)
    private String tipo;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(nullable = false)
    private int ataque;
    
    @Column(nullable = false)
    private int defensa;
    
    @Column(nullable = false)
    private int estamina;
    
    // Relación Many-to-One con Perfil
    @ManyToOne
    @JoinColumn(name = "perfil_id")
    @JsonIgnore // Evita recursión infinita
    private Perfil perfil;
    
    // Relación Many-to-Many con Habilidad (con tabla intermedia)
    @OneToMany(mappedBy = "personaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonajeHabilidad> personajeHabilidades = new ArrayList<>();
    
    // Constructores
    public Personaje() {
    }
    
    public Personaje(String nombre, String tipo, String descripcion, int ataque, int defensa, int estamina) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.ataque = ataque;
        this.defensa = defensa;
        this.estamina = estamina;
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
    
    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public int getAtaque() {
        return ataque;
    }
    
    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }
    
    public int getDefensa() {
        return defensa;
    }
    
    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }
    
    public int getEstamina() {
        return estamina;
    }
    
    public void setEstamina(int estamina) {
        this.estamina = estamina;
    }
    
    public Perfil getPerfil() {
        return perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    public List<PersonajeHabilidad> getPersonajeHabilidades() {
        return personajeHabilidades;
    }
    
    public void setPersonajeHabilidades(List<PersonajeHabilidad> personajeHabilidades) {
        this.personajeHabilidades = personajeHabilidades;
    }
    
    // Método helper para agregar habilidad con nivel
    public void agregarHabilidad(Habilidad habilidad, int nivel) {
        PersonajeHabilidad ph = new PersonajeHabilidad(this, habilidad, nivel);
        personajeHabilidades.add(ph);
    }
    
    // Método helper para remover habilidad
    public void removerHabilidad(Habilidad habilidad) {
        personajeHabilidades.removeIf(ph -> ph.getHabilidad().equals(habilidad));
    }
}