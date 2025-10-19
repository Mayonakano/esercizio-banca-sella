package it.esercizio.banca_sella.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NaturalPersonBeneficiary {
    @NotNull
    @Pattern(
            regexp = "^[A-Z]{3}[A-Z]{3}[0-9]{2}[A-EHLMPR-T][0-9]{2}[A-Z][0-9A-Z]{3}[A-Z]$",
            message = "Codice fiscale 1 non valido: formato errato"
    )
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String fiscalCode1;
    private String fiscalCode2;
    private String fiscalCode3;
    private String fiscalCode4;
    private String fiscalCode5;

    public NaturalPersonBeneficiary(String fiscalCode1) {
        this.fiscalCode1 = fiscalCode1;
    }
}


