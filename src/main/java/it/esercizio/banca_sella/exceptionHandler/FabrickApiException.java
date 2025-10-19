package it.esercizio.banca_sella.exceptionHandler;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FabrickApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String description;
    private final String rawBody;

    public FabrickApiException(HttpStatus httpStatus, String code, String description, String rawBody) {
        super(buildMessage(httpStatus, code, description));
        this.httpStatus = httpStatus;
        this.code = code;
        this.description = description;
        this.rawBody = rawBody;
    }

    private static String buildMessage(HttpStatus status, String code, String description) {
        String desc = description != null ? description : "";
        if (code != null && !code.isBlank()) {
            return String.format("Fabrick API error [%s]: %s - %s", status, code, desc);
        }
        return String.format("Fabrick API error [%s]: %s", status, desc);
    }

    /**
     * Convenience: returns true if the upstream Fabrick error code is API000 (generic error).
     */
    public boolean isApi000() {
        return code != null && code.equalsIgnoreCase("API000");
    }

}