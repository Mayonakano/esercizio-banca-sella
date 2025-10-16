package it.esercizio.banca_sella.exceptionHandler;

import org.springframework.http.HttpStatus;

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

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getRawBody() {
        return rawBody;
    }
}