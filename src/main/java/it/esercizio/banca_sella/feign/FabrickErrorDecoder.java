package it.esercizio.banca_sella.feign;

import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.List;

public class FabrickErrorDecoder implements ErrorDecoder {
    private static final Logger log = LoggerFactory.getLogger(FabrickErrorDecoder.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        String body = readBody(response);
        HttpStatus status = HttpStatus.resolve(response.status());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        try {
            // Try to parse Fabrick standard error structure
            FabrickResponse<Object> fabrick = objectMapper.readValue(body, new TypeReference<FabrickResponse<Object>>(){});
            String code = null;
            String description = null;
            List<FabrickError> errors = fabrick.getErrors();
            if (errors != null && !errors.isEmpty()) {
                code = errors.get(0).getCode();
                description = errors.get(0).getDescription();
            }
            log.warn("Feign call {} failed with status {} code {} desc {}", methodKey, status, code, description);
            return new FabrickApiException(status, code, description, body);
        } catch (Exception parseEx) {
            log.warn("Failed to parse Fabrick error body for {}. Status: {}, body: {}", methodKey, status, body);
            return new FabrickApiException(status, null, "Upstream error", body);
        }
    }

    private String readBody(Response response) {
        if (response == null || response.body() == null) return "";
        try {
            return Util.toString(response.body().asReader());
        } catch (IOException e) {
            return "";
        }
    }
}
