package com.micro11.micro11g3.service;

import com.micro11.micro11g3.model.CasoSoporte;
import com.micro11.micro11g3.model.EstadoCaso;
import com.micro11.micro11g3.repository.CasoSoporteRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class CasoSoporteServiceTest {

    @Mock
    private CasoSoporteRepository casoSoporteRepository;

    @InjectMocks
    private CasoSoporteService casoSoporteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerCasos() {
        CasoSoporte caso1 = new CasoSoporte(1, 101, "Asunto 1", LocalDateTime.now(), EstadoCaso.ABIERTO, "Mensaje 1");
        CasoSoporte caso2 = new CasoSoporte(2, 102, "Asunto 2", LocalDateTime.now(), EstadoCaso.CERRADO, "Mensaje 2");

        when(casoSoporteRepository.findAll()).thenReturn(Arrays.asList(caso1, caso2));

        List<CasoSoporte> resultado = casoSoporteService.obtenerCasos();

        assertThat(resultado).hasSize(2);
        verify(casoSoporteRepository).findAll();
    }

    @Test
    void testCrearCaso() {
        CasoSoporte caso = new CasoSoporte();
        caso.setIdUsuario(100);
        caso.setAsunto("Nuevo asunto");

        when(casoSoporteRepository.save(any(CasoSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CasoSoporte resultado = casoSoporteService.crearCaso(caso);

        assertThat(resultado.getFechaCreacion()).isNotNull();
        assertThat(resultado.getEstadoCaso()).isNotNull();
        verify(casoSoporteRepository).save(caso);
    }

    @Test
    void testActualizarEstado() {
        CasoSoporte casoExistente = new CasoSoporte(1, 100, "Asunto", LocalDateTime.now(), EstadoCaso.ABIERTO, "Mensaje");

        when(casoSoporteRepository.findById(1)).thenReturn(Optional.of(casoExistente));
        when(casoSoporteRepository.save(any(CasoSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CasoSoporte resultado = casoSoporteService.actualizarEstado(1, "CERRADO");

        assertThat(resultado.getEstadoCaso()).isEqualTo(EstadoCaso.CERRADO);
        verify(casoSoporteRepository).findById(1);
        verify(casoSoporteRepository).save(casoExistente);
    }

    @Test
    void testValidarEstado() {
        EstadoCaso estado = casoSoporteService.validarEstado("ABIERTO");
        assertThat(estado).isEqualTo(EstadoCaso.ABIERTO);
    }

    @Test
    void testCerrarCaso() {
        CasoSoporte casoExistente = new CasoSoporte(2, 200, "Asunto cerrar", LocalDateTime.now(), EstadoCaso.ABIERTO, null);

        when(casoSoporteRepository.findById(2)).thenReturn(Optional.of(casoExistente));
        when(casoSoporteRepository.save(any(CasoSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CasoSoporte resultado = casoSoporteService.cerrarCaso(2);

        assertThat(resultado.getEstadoCaso()).isEqualTo(EstadoCaso.CERRADO);
        verify(casoSoporteRepository).findById(2);
        verify(casoSoporteRepository).save(casoExistente);
    }

    @Test
    void testConsultarCaso() {
        CasoSoporte casoExistente = new CasoSoporte(3, 300, "Asunto consultar", LocalDateTime.now(), EstadoCaso.ABIERTO, null);

        when(casoSoporteRepository.findById(3)).thenReturn(Optional.of(casoExistente));

        CasoSoporte resultado = casoSoporteService.consultarCaso(3);

        assertThat(resultado).isEqualTo(casoExistente);
        verify(casoSoporteRepository).findById(3);
    }
    
}
