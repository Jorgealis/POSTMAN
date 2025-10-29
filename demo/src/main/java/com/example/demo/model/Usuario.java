package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 50)
    private String nombreUsuario;
    
    @Column(nullable = false)
    private String contrasena;
    
    @Column(nullable = false, unique = true, length = 100)
    private String correo;
    
    // Relación One-to-One con Perfil
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Perfil perfil;
    
    // Constructores
    public Usuario() {
    }
    
    public Usuario(String nombreUsuario, String contrasena, String correo) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.correo = correo;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    public String getContrasena() {
        return contrasena;
    }
    
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
    
    public String getCorreo() {
        return correo;
    }
    
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    
    public Perfil getPerfil() {
        return perfil;
    }
    
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
        if (perfil != null) {
            perfil.setUsuario(this);
        }
    }
}