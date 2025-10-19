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
        if (API_KEY == null || API_KEY.isBlank()) {
            log.warn("Fabrick API key (fabrick_client.api_key or FABRICK_API_KEY) is missing or blank. Requests will likely fail upstream.");
        }
        if (AUTH_SCHEMA == null || AUTH_SCHEMA.isBlank()) {
            log.warn("Fabrick Auth-Schema (fabrick_client.auth_schema or FABRICK_AUTH_SCHEMA) is missing or blank. Using header as blank.");
        }
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
        // Ensure JSON media types
        template.header("Accept", "application/json");
        template.header("Content-Type", "application/json");
    }
}
