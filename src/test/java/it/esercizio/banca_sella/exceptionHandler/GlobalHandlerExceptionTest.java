package it.esercizio.banca_sella.exceptionHandler;

import feign.FeignException;
import feign.Request;
import feign.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalHandlerExceptionTest {

    private final GlobalHandlerException handler = new GlobalHandlerException();

    @Test
    void handleException_nullPointer_returns404() {
        ResponseEntity<String> resp = handler.handleException();
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isEqualTo("The object doesn't exist");
    }

    @Test
    void handleIllegalArgument_returns400() {
        ResponseEntity<String> resp = handler.handleIllegalArgument();
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).isEqualTo("The parameter request is empty or wrong");
    }


    @Test
    void handleFabrickApiException_withoutCode_usesDefaultMessage() {
        FabrickApiException ex = new FabrickApiException(HttpStatus.NOT_FOUND, null, null, "{...}");
        ResponseEntity<String> resp = handler.handleFabrickApiException(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resp.getBody()).isEqualTo("Upstream Fabrick API error");
    }

    @Test
    void handleFeignException_withBody_propagatesContentAndStatus() {
        String body = "Proxy error";
        Response response = Response.builder()
                .status(502)
                .reason("Bad Gateway")
                .request(Request.create(Request.HttpMethod.GET, "/api/test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(body, StandardCharsets.UTF_8)
                .build();
        FeignException ex = FeignException.errorStatus("methodKey", response);

        ResponseEntity<String> resp = handler.handleFeignException(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_GATEWAY);
        assertThat(resp.getBody()).contains("Proxy error");
    }

    @Test
    void handleFeignException_emptyBody_returnsDefaultMessage() {
        Response response = Response.builder()
                .status(500)
                .reason("Internal Server Error")
                .request(Request.create(Request.HttpMethod.GET, "/api/test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .build();
        FeignException ex = FeignException.errorStatus("methodKey", response);

        ResponseEntity<String> resp = handler.handleFeignException(ex);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(resp.getBody()).isEqualTo("Upstream error");
    }
}
