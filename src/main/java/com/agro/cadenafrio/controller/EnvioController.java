package com.agro.cadenafrio.controller;

import com.agro.cadenafrio.dto.*;
import com.agro.cadenafrio.service.CaptchaValidatorService;
import com.agro.cadenafrio.service.EnvioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@CrossOrigin(origins = {
        "http://localhost:8081",
        "http://127.0.0.1:8081"
})
public class EnvioController {

    private final EnvioService envioService;
    private final CaptchaValidatorService captchaValidator;

    public EnvioController(EnvioService envioService,
                           CaptchaValidatorService captchaValidator) {
        this.envioService = envioService;
        this.captchaValidator = captchaValidator;
    }

    @PostMapping
    public ResponseEntity<EnvioResponse> crear(
            @RequestHeader(value = "X-Captcha-Token", required = false) String captchaToken,
            @Valid @RequestBody EnvioRequest request) {

        // Puerta de entrada del panel: sin Captcha válido no se crea nada
        if (!captchaValidator.validar(captchaToken)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Captcha inválido o ausente");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.crearEnvio(request));
    }

    @GetMapping("/{id}")
    public EnvioResponse obtener(@PathVariable Long id) {
        return envioService.obtenerEnvio(id);
    }

    @PostMapping("/{id}/eventos")
    public ResponseEntity<EventoResponse> registrarEvento(@PathVariable Long id,
                                                          @Valid @RequestBody EventoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(envioService.registrarEvento(id, request));
    }

    @GetMapping("/{id}/eventos")
    public List<EventoResponse> listarEventos(@PathVariable Long id) {
        return envioService.listarEventos(id);
    }

    @GetMapping("/{id}/alertas")
    public List<EventoResponse> listarAlertas(@PathVariable Long id) {
        return envioService.listarAlertas(id);
    }
}