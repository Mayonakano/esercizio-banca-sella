package it.esercizio.banca_sella;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
        info = @Info(title = "Banca Sella API", version = "1.0", description = "API for banking operations (balance, money transfer, transactions)"),
        tags = {
                @Tag(name = "Operations", description = "Operations for balance, transfers and transactions")
        }
)
public class BancaSellaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancaSellaApplication.class, args);
    }

}
