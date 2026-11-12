package com.agro.cadenafrio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnvioRequest(
        @NotBlank(message = "El código es obligatorio") String codigo,
        @NotBlank(message = "El producto es obligatorio") String producto,
        @NotNull(message = "La temperatura mínima es obligatoria") Double temperaturaMinima,
        @NotNull(message = "La temperatura máxima es obligatoria") Double temperaturaMaxima
) {}