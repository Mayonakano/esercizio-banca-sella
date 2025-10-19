package it.esercizio.banca_sella.feign;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import it.esercizio.banca_sella.dto.response.FabrickError;
import it.esercizio.banca_sella.dto.response.FabrickResponse;
import it.esercizio.banca_sella.exceptionHandler.FabrickApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class FabrickErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(FabrickErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        HttpStatus status = HttpStatus.resolve(response.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            // Try to parse Fabrick standard error structure and extract first error
            FabrickResponse<Object> fabrick = objectMapper.readValue(body, new TypeReference<>() {});
            String code = null;
            String description = null;
            if (fabrick != null && fabrick.getErrors() != null && !fabrick.getErrors().isEmpty()) {
                FabrickError first = fabrick.getErrors().get(0);
                code = first.getCode();
                description = first.getDescription();
            }
            if (code == null && (description == null || description.isBlank())) {
                description = response.reason();
            }
            log.warn("Feign call {} failed with status: {}, code: {}, description: {}", methodKey, status, code, description);
            return new FabrickApiException(status, code, description, body);
        } catch (Exception parseEx) {
            log.warn("Failed to parse Fabrick error body for {}. Status: {}, body: {}", methodKey, status, body);
            return new FabrickApiException(status, null, "Upstream error", body);
        }
    }

    private String readBody(Response response) {
        if (response == null || response.body() == null) {
            throw new NullPointerException("Response body is null or empty");
        }
        try {
            return Util.toString(response.body().asReader());
        } catch (IOException e) {
            throw new IllegalArgumentException("Impossible to read response body", e);
        }
    }
}
