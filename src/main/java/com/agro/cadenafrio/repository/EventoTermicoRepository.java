package com.agro.cadenafrio.repository;

import com.agro.cadenafrio.model.EventoTermico;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoTermicoRepository extends JpaRepository<EventoTermico, Long> {

    List<EventoTermico> findByEnvioIdOrderByFechaHoraAsc(Long envioId);

    List<EventoTermico> findByEnvioIdAndAlertaTrueOrderByFechaHoraAsc(Long envioId);
} 