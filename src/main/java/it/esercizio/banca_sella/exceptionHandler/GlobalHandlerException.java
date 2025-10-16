package it.esercizio.banca_sella.exceptionHandler;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandlerException {

    private static final Logger log = LoggerFactory.getLogger(GlobalHandlerException.class);

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
        log.warn("Fabrick API exception: status={}, code={}, description={}", ex.getHttpStatus(), ex.getCode(), ex.getDescription());
        String body = ex.getCode() != null ? ex.getCode() + ": " + ex.getDescription() : ex.getDescription();
        if (body == null || body.isBlank()) {
            body = "Upstream Fabrick API error";
        }
        return new ResponseEntity<>(body, ex.getHttpStatus());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<String> handleFeignException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        String content = ex.contentUTF8();
        log.warn("Feign exception: status={}, body={}", status, content);
        String body = (content != null && !content.isBlank()) ? content : "Upstream error";
        return new ResponseEntity<>(body, status);
    }
}
