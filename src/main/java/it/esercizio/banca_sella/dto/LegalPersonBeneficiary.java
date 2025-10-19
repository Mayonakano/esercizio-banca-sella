package it.esercizio.banca_sella.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalPersonBeneficiary {
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String fiscalCode;
    private String legalRepresentativeFiscalCode;
}
