package it.esercizio.banca_sella.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.TimeZone;
import java.util.UUID;

@Component
public class FeignClientAuthInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignClientAuthInterceptor.class);

    @Value("${fabrick_client.api_key}")
    private String API_KEY;

    @Value("${fabrick_client.auth_schema}")
    private String AUTH_SCHEMA;

    @Override
    public void apply(RequestTemplate template) {
        // Use canonical header casing used by Fabrick docs
        template.header("Api-Key", API_KEY);
        // Keep lowercase for safety (gateways treat header names as case-insensitive)
        template.header("apikey", API_KEY);
        template.header("Auth-Schema", AUTH_SCHEMA);
        // Fabrick required headers
        String timeZone = TimeZone.getDefault() != null ? TimeZone.getDefault().getID() : "Europe/Rome";
        template.header("X-Time-Zone", timeZone);
        String requestId = UUID.randomUUID().toString();
        template.header("X-Request-Id", requestId);
        log.debug("Feign request -> X-Request-Id: {} X-Time-Zone: {}", requestId, timeZone);
        // Ensure JSON media types
        template.header("Accept", "application/json");
        template.header("Content-Type", "application/json");
    }
}
