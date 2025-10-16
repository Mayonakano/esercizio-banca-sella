package it.esercizio.banca_sella.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LegalPersonBeneficiary {
    @NotNull
    private String fiscalCode;
    private String legalRepresentativeFiscalCode;
}
