package com.micro11.micro11g3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro11.micro11g3.model.*;
import com.micro11.micro11g3.service.CasoSoporteService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CasoSoporteController.class)
class CasoSoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CasoSoporteService casoSoporteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerCasos_conDatos() throws Exception {
        CasoSoporte c1 = crearCasoEjemplo(1, EstadoCaso.ABIERTO);
        CasoSoporte c2 = crearCasoEjemplo(2, EstadoCaso.RESUELTO);

        Mockito.when(casoSoporteService.obtenerCasos()).thenReturn(List.of(c1, c2));

        mockMvc.perform(get("/api/casos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCaso").value(1))
                .andExpect(jsonPath("$[1].estadoCaso").value("RESUELTO"));
    }

    @Test
    void testObtenerCasos_sinDatos() throws Exception {
        Mockito.when(casoSoporteService.obtenerCasos()).thenReturn(List.of());

        mockMvc.perform(get("/api/casos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testConsultarCaso_existente() throws Exception {
        CasoSoporte caso = crearCasoEjemplo(1, EstadoCaso.ABIERTO);

        Mockito.when(casoSoporteService.consultarCaso(eq(1))).thenReturn(caso);

        mockMvc.perform(get("/api/casos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCaso").value(1));
    }

    @Test
    void testConsultarCaso_noExiste() throws Exception {
        Mockito.when(casoSoporteService.consultarCaso(eq(999))).thenReturn(null);

        mockMvc.perform(get("/api/casos/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCrearCaso_ok() throws Exception {
        CasoSoporte entrada = crearCasoEjemplo(0, null);
        CasoSoporte guardado = crearCasoEjemplo(10, EstadoCaso.ABIERTO);

        Mockito.when(casoSoporteService.crearCaso(any(CasoSoporte.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/casos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(entrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCaso").value(10));
    }

    @Test
    void testCerrarCaso_existente() throws Exception {
        CasoSoporte cerrado = crearCasoEjemplo(1, EstadoCaso.CERRADO);

        Mockito.when(casoSoporteService.cerrarCaso(eq(1))).thenReturn(cerrado);

        mockMvc.perform(post("/api/casos/cerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCaso\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoCaso").value("CERRADO"));
    }

    @Test
    void testCerrarCaso_noExiste() throws Exception {
        Mockito.when(casoSoporteService.cerrarCaso(eq(999))).thenReturn(null);

        mockMvc.perform(post("/api/casos/cerrar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCaso\":999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarEstado_ok() throws Exception {
        CasoSoporte actualizado = crearCasoEjemplo(1, EstadoCaso.RESUELTO);

        Mockito.when(casoSoporteService.consultarCaso(eq(1)))
                .thenReturn(actualizado);
        Mockito.when(casoSoporteService.actualizarEstadoCaso(eq(1), eq("RESUELTO")))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/casos/actualizarEstado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCaso\":1,\"nuevoEstado\":\"RESUELTO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoCaso").value("RESUELTO"));
    }

    @Test
    void testActualizarEstado_casoNoExiste() throws Exception {
        Mockito.when(casoSoporteService.consultarCaso(eq(999))).thenReturn(null);

        mockMvc.perform(put("/api/casos/actualizarEstado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCaso\":999,\"nuevoEstado\":\"CERRADO\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarEstado_estadoInvalido() throws Exception {
        CasoSoporte existente = crearCasoEjemplo(1, EstadoCaso.ABIERTO);

        Mockito.when(casoSoporteService.consultarCaso(eq(1))).thenReturn(existente);
        Mockito.when(casoSoporteService.actualizarEstadoCaso(eq(1), eq("INVALIDO")))
                .thenReturn(null);

        mockMvc.perform(put("/api/casos/actualizarEstado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idCaso\":1,\"nuevoEstado\":\"INVALIDO\"}"))
                .andExpect(status().isBadRequest());
    }

    private CasoSoporte crearCasoEjemplo(int id, EstadoCaso estado) {
        return new CasoSoporte(
                id,
                123,
                "Problema de ejemplo",
                LocalDateTime.now(),
                estado != null ? estado : EstadoCaso.ABIERTO,
                "mensaje de prueba"
        );
    }
}
