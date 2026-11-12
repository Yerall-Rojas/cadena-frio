package com.agro.cadenafrio.dto;

import com.agro.cadenafrio.model.EventoTermico;

import java.time.LocalDateTime;

public record EventoResponse(
        Long id,
        Long envioId,
        Double temperatura,
        LocalDateTime fechaHora,
        boolean alerta
) {
    public static EventoResponse desde(EventoTermico ev) {
        return new EventoResponse(
                ev.getId(), ev.getEnvio().getId(),
                ev.getTemperatura(), ev.getFechaHora(), ev.isAlerta());
    }
}