package com.agro.cadenafrio.repository;

import com.agro.cadenafrio.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    boolean existsByCodigo(String codigo);
}