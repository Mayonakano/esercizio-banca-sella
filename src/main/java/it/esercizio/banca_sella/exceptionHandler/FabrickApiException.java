package it.esercizio.banca_sella.exceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class FabrickApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String description;
    private final String rawBody;

    private static String buildMessage(HttpStatus status, String code, String description) {
        String desc = description != null ? description : "";
        if (code != null && !code.isBlank()) {
            return String.format("Fabrick API error [%s]: %s - %s", status, code, desc);
        }
        return String.format("Fabrick API error [%s]: %s", status, desc);
    }

}