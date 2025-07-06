package com.micro11.micro11g3.assembler;

import com.micro11.micro11g3.controller.CasoSoporteController;
import com.micro11.micro11g3.model.CasoSoporte;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CasoSoporteAssembler implements RepresentationModelAssembler<CasoSoporte, EntityModel<CasoSoporte>> {

    @Override
    @NonNull
    public EntityModel<CasoSoporte> toModel(@NonNull CasoSoporte casoSoporte) {
        return EntityModel.of(casoSoporte,
            linkTo(methodOn(CasoSoporteController.class).obtenerCasos()).withRel("casos"),
            linkTo(methodOn(CasoSoporteController.class).consultarCaso(casoSoporte.getIdCaso())).withSelfRel(),
            linkTo(methodOn(CasoSoporteController.class).crearCaso(casoSoporte)).withRel("crear"),
            linkTo(methodOn(CasoSoporteController.class).cerrarCaso(new CasoSoporteController.IdRequest() {{
                idCaso = casoSoporte.getIdCaso();
            }})).withRel("cerrar"),
            linkTo(methodOn(CasoSoporteController.class).actualizarEstado(new CasoSoporteController.ActualizarEstadoRequest() {{
                idCaso = casoSoporte.getIdCaso();
                nuevoEstado = casoSoporte.getEstadoCaso().name();
            }})).withRel("actualizarEstado")
        );
    }
}
