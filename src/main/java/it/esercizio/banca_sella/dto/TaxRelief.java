package it.esercizio.banca_sella.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRelief {

    private String taxReliefId;
    @NotNull
    @JsonProperty("isCondoUpgrade")
    private Boolean isCondoUpgrade;
    @NotNull
    private String creditorFiscalCode;
    @NotNull
    private String beneficiaryType;

    private NaturalPersonBeneficiary naturalPersonBeneficiary;
    private LegalPersonBeneficiary legalPersonBeneficiary;

}
