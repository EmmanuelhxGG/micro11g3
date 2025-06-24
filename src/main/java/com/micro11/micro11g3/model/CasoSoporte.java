package com.micro11.micro11g3.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "caso_soporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasoSoporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCaso;

    @Column(name = "id_usuario", nullable = false)
    private int idUsuario;

    @Column(name = "asunto", length = 200, nullable = false)
    private String asunto;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_caso", length = 80, nullable = false)
    private EstadoCaso estadoCaso;

    @Column(name = "mensaje_consulta", length = 1000, nullable = true)
    private String mensajeConsulta;
}
