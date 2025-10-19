package it.esercizio.banca_sella.feign;

import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class FeignClientAuthInterceptorTest {

    @Test
    void apply_sets_expected_headers() {
        FeignClientAuthInterceptor interceptor = new FeignClientAuthInterceptor();
        ReflectionTestUtils.setField(interceptor, "API_KEY", "test-key");
        ReflectionTestUtils.setField(interceptor, "AUTH_SCHEMA", "S2S");

        RequestTemplate template = new RequestTemplate();
        interceptor.apply(template);

        assertThat(template.headers()).containsKeys("Api-Key", "apikey", "Auth-Schema", "X-Time-Zone", "X-Request-Id", "Accept", "Content-Type");
        assertThat(template.headers().get("Api-Key")).contains("test-key");
        assertThat(template.headers().get("apikey")).contains("test-key");
        assertThat(template.headers().get("Auth-Schema")).contains("S2S");
        // dynamic values exist and non-empty
        assertThat(template.headers().get("X-Time-Zone")).isNotNull();
        assertThat(template.headers().get("X-Request-Id")).isNotNull();
        assertThat(template.headers().get("Accept")).isNotNull();
        assertThat(template.headers().get("Content-Type")).isNotNull();
    }
}
