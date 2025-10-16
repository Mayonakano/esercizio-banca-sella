package it.esercizio.banca_sella.dto.common;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Creditor {

    @NotNull
    private String name;
    @NotNull
    private Account account;
    private Address address;

}
