package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "perfiles")
public class Perfil {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 500)
    private String biografia;
    
    @Column(length = 255)
    private String avatar; // URL de la imagen
    
    // Relación One-to-One con Usuario
    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    @JsonIgnore // Evita recursión infinita en JSON
    private Usuario usuario;
    
    // Relación One-to-Many con Personaje
    @OneToMany(mappedBy = "perfil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Personaje> personajes = new ArrayList<>();
    
    // Constructores
    public Perfil() {
    }
    
    public Perfil(String biografia, String avatar) {
        this.biografia = biografia;
        this.avatar = avatar;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getBiografia() {
        return biografia;
    }
    
    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public List<Personaje> getPersonajes() {
        return personajes;
    }
    
    public void setPersonajes(List<Personaje> personajes) {
        this.personajes = personajes;
    }
    
    // Método helper para agregar personaje
    public void agregarPersonaje(Personaje personaje) {
        personajes.add(personaje);
        personaje.setPerfil(this);
    }
    
    // Método helper para remover personaje
    public void removerPersonaje(Personaje personaje) {
        personajes.remove(personaje);
        personaje.setPerfil(null);
    }
}