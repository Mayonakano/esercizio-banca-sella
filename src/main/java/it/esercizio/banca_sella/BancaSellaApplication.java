package it.esercizio.banca_sella;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BancaSellaApplication {

    public static void main(String[] args) {
        SpringApplication.run(BancaSellaApplication.class, args);
    }

}
