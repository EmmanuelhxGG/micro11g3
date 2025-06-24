package com.micro11.micro11g3.service;

import com.micro11.micro11g3.model.CasoSoporte;
import com.micro11.micro11g3.model.EstadoCaso;
import com.micro11.micro11g3.repository.CasoSoporteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CasoSoporteService {

    @Autowired
    private CasoSoporteRepository casoSoporteRepository;

    public List<CasoSoporte> obtenerCasos() {
        return casoSoporteRepository.findAll(); //listar casos
    }

    public CasoSoporte crearCaso(CasoSoporte caso) {
        caso.setFechaCreacion(LocalDateTime.now());
        if (caso.getEstadoCaso() == null) {
            caso.setEstadoCaso(EstadoCaso.ABIERTO); //Se asigna abierto por defecto
        }
        return casoSoporteRepository.save(caso);
    }

    public CasoSoporte actualizarEstado(int idCaso, String nuevoEstadoStr) {
        CasoSoporte caso = casoSoporteRepository.findById(idCaso).orElse(null);
        if (caso == null) {
            return null;
        }

        EstadoCaso nuevoEstado = validarEstado(nuevoEstadoStr);
        if (nuevoEstado == null) {
            return null;
        }

        caso.setEstadoCaso(nuevoEstado);
        return casoSoporteRepository.save(caso);
    }

    public EstadoCaso validarEstado(String estadoStr) {
        if (estadoStr == null) {
            return null;
        }
        for (EstadoCaso estado : EstadoCaso.values()) {
            if (estado.name().equalsIgnoreCase(estadoStr)) {
                return estado;
            }
        }
        return null;
    }


    public CasoSoporte cerrarCaso(int idCaso) {
        return actualizarEstado(idCaso, "CERRADO");
    }

    public CasoSoporte consultarCaso(int idCaso) {
        return casoSoporteRepository.findById(idCaso).orElse(null);
    }
}
