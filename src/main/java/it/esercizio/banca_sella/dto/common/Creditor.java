package it.esercizio.banca_sella.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Creditor {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Account account;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Address address;

}
