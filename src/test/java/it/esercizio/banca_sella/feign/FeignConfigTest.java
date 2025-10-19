package it.esercizio.banca_sella.feign;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FeignConfigTest {

    @Test
    void beans_are_configured_as_expected() {
        FeignConfig config = new FeignConfig();
        ErrorDecoder decoder = config.errorDecoder();
        Logger.Level level = config.feignLoggerLevel();

        assertThat(decoder).isInstanceOf(FabrickErrorDecoder.class);
        assertThat(level).isEqualTo(Logger.Level.BASIC);
    }
}
