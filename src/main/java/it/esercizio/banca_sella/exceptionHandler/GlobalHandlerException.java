package it.esercizio.banca_sella.exceptionHandler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import it.esercizio.banca_sella.dto.response.FabrickError;
import it.esercizio.banca_sella.dto.response.FabrickResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalHandlerException {

    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("The object doesn't exist", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument() {
        return new ResponseEntity<>("The parameter request is empty or wrong", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FabrickApiException.class)
    public ResponseEntity<String> handleFabrickApiException(FabrickApiException ex) {
        String rowBody = ex.getRawBody();
        // Log all errors if present in the raw body, but do not alter the response contract
        if (rowBody != null && !rowBody.isBlank()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
                FabrickResponse fabrick = mapper.readValue(rowBody, FabrickResponse.class);
                List<FabrickError> errors = fabrick.getErrors();
                if (errors != null && !errors.isEmpty()) {
                    for (int i = 0; i < errors.size(); i++) {
                        FabrickError err = errors.get(i);
                        log.error("Fabrick error [{}]: code={}, description={}, params={}", i, err.getCode(), err.getDescription(), err.getParams());
                    }
                }
            } catch (Exception parseEx) {
                log.debug("Could not parse Fabrick raw error body to list all errors. Body: {}", rowBody, parseEx);
            }
        }
        // Build the client-visible body from the exception fields
        String body = ex.getCode() != null && ex.getDescription() != null
                ? ex.getCode() + ": " + ex.getDescription()
                : "Upstream Fabrick API error";
        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        String content = ex.contentUTF8();
        log.warn("Feign exception: status={}, body={}", status, content);
        String body = (content != null && !content.isBlank()) ? content : "Upstream error";
        return new ResponseEntity<>(body, status);
    }
}
