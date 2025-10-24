package com.example.demo.repository;

import java.util.Arrays;
import java.util.List;

import com.example.demo.model.Habilidad;
import com.example.demo.model.Personaje;

public class Datosjuego {
    public static final List<Habilidad> HABILIDADES = new java.util.ArrayList<>(Arrays.asList(
    new Habilidad(1, "Espadazo",
      "Un ataque poderoso con la espada.", 10, 0, -5),
    new Habilidad(2, "Escudo de Hierro",
      "Aumenta la defensa del guerrero.", 0, 15, -3),
    new Habilidad(3,"agua de la vida","recupera PV al instante",0,0,20),
    new Habilidad(4,"Bola de fuego","lanza una bola de fuego al enemigo",15,0,-10
    ),
    new Habilidad(5,"muerte sin retorno","ataque letal que acaba con el enemigo instantaneamente",100,0,-50)
  ));

  public static final List<Personaje> PERSONAJES = new java.util.ArrayList<>(Arrays.asList(
    new Personaje(1, "Gagh-Ar", "guerrero",
      "Un valiente luchador con gran fuerza física.",
      80, 70, 60, Arrays.asList(1, 2)),
    new Personaje(2, "Elyra", "maga",
      "Hechicera con gran dominio de la energía mágica.",
      65, 40, 90, Arrays.asList(2)),
    new Personaje(3, "Thalor", "sanador",
      "Un sabio sanador que protege a sus aliados.",
      50, 60, 100, Arrays.asList(3)),
    new Personaje(4, "Zarak", "mago", "un mago muy curioso",70,50,80,Arrays.asList(4) ),
    new Personaje(5, "Krell", "asesino", "un asesino sigiloso y letal",90,40,70,Arrays.asList(5) )
  ));

}

