package com.agro.cadenafrio.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Service
public class CaptchaValidatorService {

    private static final Logger log = LoggerFactory.getLogger(CaptchaValidatorService.class);

    private final String secret;
    private final String verifyUrl;
    private final RestClient restClient;

    public CaptchaValidatorService(@Value("${captcha.secret}") String secret,
                                   @Value("${captcha.verify-url}") String verifyUrl) {
        this.secret = secret;
        this.verifyUrl = verifyUrl;
        this.restClient = RestClient.create();
    }

    public boolean validar(String token) {
        // Sin token no hay nada que verificar
        if (token == null || token.isBlank()) {
            return false;
        }

        MultiValueMap<String, String> formulario = new LinkedMultiValueMap<>();
        formulario.add("secret", secret);
        formulario.add("response", token);

        try {
            Map<String, Object> respuesta = restClient.post()
                    .uri(verifyUrl)
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(formulario)
                    .retrieve()
                    .body(new ParameterizedTypeReference<Map<String, Object>>() {});

            return respuesta != null && Boolean.TRUE.equals(respuesta.get("success"));
        } catch (RestClientException e) {
            // Si no se puede contactar a Google, se rechaza por seguridad
            log.error("No se pudo verificar el Captcha: {}", e.getMessage());
            return false;
        }
    }
}