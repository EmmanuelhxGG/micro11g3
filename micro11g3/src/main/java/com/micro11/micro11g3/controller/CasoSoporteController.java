package com.micro11.micro11g3.controller;

import com.micro11.micro11g3.model.CasoSoporte;
import com.micro11.micro11g3.service.CasoSoporteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casos")
public class CasoSoporteController {

    @Autowired
    private CasoSoporteService casoSoporteService;

    @GetMapping //listar todos los casos
    public ResponseEntity<List<CasoSoporte>> obtenerCasos() {
        List<CasoSoporte> casos = casoSoporteService.obtenerCasos();
        if (casos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(casos, HttpStatus.OK);
    }

    @GetMapping("/{idCaso}") //listar un caso por id
    public ResponseEntity<CasoSoporte> consultarCaso(@PathVariable int idCaso) {
        CasoSoporte caso = casoSoporteService.consultarCaso(idCaso);
        if (caso == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(caso, HttpStatus.OK);
    }

    @PostMapping("/crear") //crear un caso
    public ResponseEntity<CasoSoporte> crearCaso(@RequestBody CasoSoporte caso) {
        CasoSoporte nuevoCaso = casoSoporteService.crearCaso(caso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCaso);
    }

    public static class IdRequest {
        public int idCaso;
    }

    @PostMapping("/cerrar") //cerrar un caso
    public ResponseEntity<CasoSoporte> cerrarCaso(@RequestBody IdRequest request) {
        CasoSoporte casoCerrado = casoSoporteService.cerrarCaso(request.idCaso);
        if (casoCerrado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(casoCerrado, HttpStatus.OK);
    }

    public static class ActualizarEstadoRequest {
        public int idCaso;
        public String nuevoEstado;
    }

    @PutMapping("/actualizarEstado") //actualizar el estado de un caso
    public ResponseEntity<?> actualizarEstado(@RequestBody ActualizarEstadoRequest request) {
        CasoSoporte casoExistente = casoSoporteService.consultarCaso(request.idCaso);
        if (casoExistente == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        CasoSoporte casoActualizado = casoSoporteService.actualizarEstado(request.idCaso, request.nuevoEstado);
        if (casoActualizado == null) {
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return new ResponseEntity<>(casoActualizado, HttpStatus.OK);
    }

}
