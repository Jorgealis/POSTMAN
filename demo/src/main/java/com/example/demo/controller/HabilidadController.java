package com.example.demo.controller;


import com.example.demo.repository.Datosjuego;
import com.example.demo.model.Habilidad;


import org.springframework.http.ResponseEntity;
import  org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Comparator;

@RestController
@RequestMapping("/api/habilidades")

public class HabilidadController {

  @GetMapping
  public List<Habilidad> obtenerHabilidades(
      @RequestParam(required = false) String nombre) {

    if (nombre != null) {
      return Datosjuego.HABILIDADES.stream()
        .filter(p -> p.getNombre() 
            .toLowerCase()
            .contains(nombre.toLowerCase()))
        .toList();
    }
    return Datosjuego.HABILIDADES;
  }
// este getmapping es especificamente para obetener para obetener por ID
  @GetMapping("/{id}")
  public Habilidad obtenerPorId(@PathVariable int id) {
    return Datosjuego.HABILIDADES.stream()
      .filter(p -> p.getId() == id)
      .findFirst()
      .orElse(null);
  }

  //Ahora viendo que se pueden tener multiples getMapping hare otro para hacer el ordenamiento por incremento de estamina ya sea 
  //de mayor o menor modo
 @GetMapping("/ordenar")
 public List<Habilidad> ordenporEstamina(
  @RequestParam(required=false) String sort,
  @RequestParam(required=false,defaultValue="asc") String order){
    List<Habilidad> habilidades =new java.util.ArrayList<>(Datosjuego.HABILIDADES);

    if("incrementoEstamina".equalsIgnoreCase(sort)){
      if("asc".equalsIgnoreCase(order)){
        habilidades.sort(Comparator.comparingInt(Habilidad::getIncrementoEstamina));

      } else{
        habilidades.sort(Comparator.comparingInt(Habilidad::getIncrementoEstamina).reversed());
      }
    }
    return habilidades;
  }

  
  //Añado el post para poder crearlos porque sino mi programa no sabe como hacerlo
  @PostMapping
  public ResponseEntity<Habilidad> crearHabilidad(@RequestBody Habilidad habilidad){
    Datosjuego.HABILIDADES.add(habilidad);
    return ResponseEntity.ok(habilidad);
  }
   //añado el PUT para actualizacionessssss

   @PutMapping("/{id}")
    public ResponseEntity<Habilidad> actualizarParcialH(@PathVariable int id,
            @RequestBody Map<String, Object> actualizaciones) {
        for (Habilidad habilidad : Datosjuego.HABILIDADES) {
            if (habilidad.getId() == id) {
                for (Map.Entry<String, Object> entry : actualizaciones.entrySet()) {
                    switch (entry.getKey()) {
                        case "nombre":
                            habilidad.setNombre((String) entry.getValue());
                            break;
                        case "descripcion":
                            habilidad.setDescripcion((String) entry.getValue());
                            break;
                        case "incrementoAtaque":
                            habilidad.setIncrementoAtaque((Integer) entry.getValue());
                            break;
                        case "incrementoDefensa":
                            habilidad.setIncrementoDefensa((Integer) entry.getValue());
                            break;
                        case "incrementoEstamina":
                            habilidad.setIncrementoEstamina((Integer) entry.getValue());
                            break;              
                        // Puedes agregar más campos aquí si lo necesitas
                    }
                }
                return ResponseEntity.ok(habilidad);
            }
        }
        return ResponseEntity.notFound().build();
    }

    //Ahora vamos con el DELETE

     @DeleteMapping("/{id}")
    public ResponseEntity<Habilidad> eliminarPersonaje(@PathVariable int id){
        Iterator<Habilidad> iterator=Datosjuego.HABILIDADES.iterator();
        while (iterator.hasNext()){
            Habilidad habilidad=iterator.next();
            if (habilidad.getId()==id){
                iterator.remove();//aqui quitamos nuestra fucking habilidad de la lista
                return ResponseEntity.ok().build();//devolvemos una respuesta exitosa sin contenido
            }
        }
        return ResponseEntity.notFound().build();//si no encontramos la fucking habilidad devolvemos 404
    }

}
