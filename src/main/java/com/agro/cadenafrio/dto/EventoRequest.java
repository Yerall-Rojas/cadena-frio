package com.agro.cadenafrio.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventoRequest(
        @NotNull(message = "La temperatura es obligatoria") Double temperatura,
        LocalDateTime fechaHora
) {}