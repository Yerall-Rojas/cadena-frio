package com.agro.cadenafrio.dto;

import com.agro.cadenafrio.model.Envio;

import java.time.LocalDateTime;

public record EnvioResponse(
        Long id,
        String codigo,
        String producto,
        Double temperaturaMinima,
        Double temperaturaMaxima,
        String estado,
        LocalDateTime fechaCreacion
) {
    public static EnvioResponse desde(Envio e) {
        return new EnvioResponse(
                e.getId(), e.getCodigo(), e.getProducto(),
                e.getTemperaturaMinima(), e.getTemperaturaMaxima(),
                e.getEstado(), e.getFechaCreacion());
    }
}