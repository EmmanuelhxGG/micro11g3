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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
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
void testCrearCasoConEstadoYaDefinido() {
    CasoSoporte caso = new CasoSoporte();
    caso.setIdUsuario(100);
    caso.setAsunto("Asunto");
    caso.setEstadoCaso(EstadoCaso.ABIERTO); 

    when(casoSoporteRepository.save(any(CasoSoporte.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CasoSoporte resultado = casoSoporteService.crearCaso(caso);

    assertThat(resultado.getEstadoCaso()).isEqualTo(EstadoCaso.ABIERTO);
    verify(casoSoporteRepository).save(caso);
}

@Test
void testActualizarEstadoCasoNoExiste() {
    when(casoSoporteRepository.findById(99)).thenReturn(Optional.empty());

    CasoSoporte resultado = casoSoporteService.actualizarEstado(99, "CERRADO");

    assertThat(resultado).isNull();
}

@Test
void testActualizarEstadoEstadoInvalido() {
    CasoSoporte casoExistente = new CasoSoporte(1, 100, "Asunto", LocalDateTime.now(), EstadoCaso.ABIERTO, "Mensaje");
    when(casoSoporteRepository.findById(1)).thenReturn(Optional.of(casoExistente));

    CasoSoporte resultado = casoSoporteService.actualizarEstado(1, "INVALIDO");

    assertThat(resultado).isNull();
}

@Test
void testValidarEstadoNull() {
    EstadoCaso estado = casoSoporteService.validarEstado(null);
    assertThat(estado).isNull();
}

@Test
void testValidarEstadoNoCoincide() {
    EstadoCaso estado = casoSoporteService.validarEstado("INEXISTENTE");
    assertThat(estado).isNull();
}

@Test
void testConsultarCasoNoExiste() {
    when(casoSoporteRepository.findById(123)).thenReturn(Optional.empty());

    CasoSoporte resultado = casoSoporteService.consultarCaso(123);

    assertThat(resultado).isNull();
}

}
