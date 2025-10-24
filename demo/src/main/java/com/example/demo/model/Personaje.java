package com.example.demo.model;

import java.util.List;

public class Personaje {
    private int id;
    private String nombre;
    private String tipo;
    private String descripcion;
    private int ataque;
    private int defensa;
    private int estamina;
    private List<Integer> habilidades;


     public Personaje(int id, String nombre, String tipo, String descripcion, int ataque, int defensa, int estamina,
            List<Integer> habilidades) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.ataque = ataque;
        this.defensa = defensa;
        this.estamina = estamina;
        this.habilidades = habilidades;
    }
    //getters
    public int getId() {
        return id;
    }
    public String getNombre() {
        return nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public int getAtaque() {
        return ataque;
    }
    public int getDefensa() {
        return defensa;
    }
    public int getEstamina() {
        return estamina;
    }
    //setters
    public List<Integer> getHabilidades() {
        return habilidades;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public void setDefensa(int defensa) {
        this.defensa = defensa;
    }

    public void setEstamina(int estamina) {
        this.estamina = estamina;
    }

    public void setHabilidades(List<Integer> habilidades) {
        this.habilidades = habilidades;
    }

    // Constructor vacío
    public Personaje() {
    }

    //.....................Constructor.........

}
