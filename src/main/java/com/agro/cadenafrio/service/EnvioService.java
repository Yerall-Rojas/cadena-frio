package com.agro.cadenafrio.service;

import com.agro.cadenafrio.dto.*;
import com.agro.cadenafrio.model.Envio;
import com.agro.cadenafrio.model.EventoTermico;
import com.agro.cadenafrio.repository.EnvioRepository;
import com.agro.cadenafrio.repository.EventoTermicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final EventoTermicoRepository eventoRepository;

    public EnvioService(EnvioRepository envioRepository,
                        EventoTermicoRepository eventoRepository) {
        this.envioRepository = envioRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public EnvioResponse crearEnvio(EnvioRequest req) {
        // Regla 1: el rango debe tener sentido
        if (req.temperaturaMinima() >= req.temperaturaMaxima()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La temperatura mínima debe ser menor que la máxima");
        }
        // Regla 2: el código no se puede repetir
        if (envioRepository.existsByCodigo(req.codigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Ya existe un envío con el código " + req.codigo());
        }

        Envio envio = new Envio();
        envio.setCodigo(req.codigo());
        envio.setProducto(req.producto());
        envio.setTemperaturaMinima(req.temperaturaMinima());
        envio.setTemperaturaMaxima(req.temperaturaMaxima());

        return EnvioResponse.desde(envioRepository.save(envio));
    }

    @Transactional(readOnly = true)
    public EnvioResponse obtenerEnvio(Long id) {
        return EnvioResponse.desde(buscarEnvio(id));
    }

    @Transactional
    public EventoResponse registrarEvento(Long envioId, EventoRequest req) {
        Envio envio = buscarEnvio(envioId);

        // si la temperatura sale del rango permitido, es una alerta
        boolean fueraDeRango = req.temperatura() < envio.getTemperaturaMinima()
                            || req.temperatura() > envio.getTemperaturaMaxima();

        EventoTermico evento = new EventoTermico();
        evento.setEnvio(envio);
        evento.setTemperatura(req.temperatura());
        evento.setFechaHora(req.fechaHora() != null ? req.fechaHora() : LocalDateTime.now());
        evento.setAlerta(fueraDeRango);

        return EventoResponse.desde(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarEventos(Long envioId) {
        buscarEnvio(envioId); // si el envío no existe, lanza 404
        return eventoRepository.findByEnvioIdOrderByFechaHoraAsc(envioId)
                .stream().map(EventoResponse::desde).toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponse> listarAlertas(Long envioId) {
        buscarEnvio(envioId);
        return eventoRepository.findByEnvioIdAndAlertaTrueOrderByFechaHoraAsc(envioId)
                .stream().map(EventoResponse::desde).toList();
    }

    private Envio buscarEnvio(Long id) {
        return envioRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "No existe el envío con id " + id));
    }
}