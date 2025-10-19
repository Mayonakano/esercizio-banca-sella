package it.esercizio.banca_sella.feign;

import feign.Request;
import feign.Response;
import it.esercizio.banca_sella.exceptionHandler.FabrickApiException;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class FabrickErrorDecoderTest {

    @Test
    void decode_parses_fabrick_error_body() {
        String json = """
                {
                  "status": "KO",
                  "errors": [{
                    "code": "REQ001",
                    "description": "Invalid request"
                  }]
                }""";
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.GET, "/api/test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(json, StandardCharsets.UTF_8)
                .build();

        FabrickErrorDecoder decoder = new FabrickErrorDecoder();
        Exception ex = decoder.decode("methodKey", response);
        assertThat(ex).isInstanceOf(FabrickApiException.class);
        FabrickApiException fae = (FabrickApiException) ex;
        assertThat(fae.getHttpStatus().value()).isEqualTo(400);
        assertThat(fae.getCode()).isEqualTo("REQ001");
        assertThat(fae.getDescription()).isEqualTo("Invalid request");
        assertThat(fae.getRawBody()).contains("Invalid request");
        // message built via FabrickApiException.buildMessage
        assertThat(fae.getMessage()).isEqualTo("Fabrick API error [400 BAD_REQUEST]: REQ001 - Invalid request");
    }

    @Test
    void decode_parses_API000_code() {
        String json = """
                {
                  "status": "KO",
                  "errors": [{
                    "code": "API000",
                    "description": "Generic error"
                  }]
                }""";
        Response response = Response.builder()
                .status(400)
                .reason("Bad Request")
                .request(Request.create(Request.HttpMethod.POST, "/api/test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(json, StandardCharsets.UTF_8)
                .build();

        FabrickErrorDecoder decoder = new FabrickErrorDecoder();
        Exception ex = decoder.decode("methodKey", response);
        assertThat(ex).isInstanceOf(FabrickApiException.class);
        FabrickApiException fae = (FabrickApiException) ex;
        assertThat(fae.getHttpStatus().value()).isEqualTo(400);
        assertThat(fae.getCode()).isEqualTo("API000");
        assertThat(fae.getDescription()).isEqualTo("Generic error");
        assertThat(fae.isApi000()).isTrue();
    }

    @Test
    void decode_handles_non_json_body() {
        String text = "Upstream failed";
        Response response = Response.builder()
                .status(502)
                .reason("Bad Gateway")
                .request(Request.create(Request.HttpMethod.GET, "/api/test", Collections.emptyMap(), null, StandardCharsets.UTF_8, null))
                .body(text, StandardCharsets.UTF_8)
                .build();

        FabrickErrorDecoder decoder = new FabrickErrorDecoder();
        Exception ex = decoder.decode("methodKey", response);
        assertThat(ex).isInstanceOf(FabrickApiException.class);
        FabrickApiException fae = (FabrickApiException) ex;
        assertThat(fae.getHttpStatus().value()).isEqualTo(502);
        assertThat(fae.getCode()).isNull();
        assertThat(fae.getDescription()).isEqualTo("Upstream error");
        assertThat(fae.getRawBody()).contains("Upstream failed");
    }
}
