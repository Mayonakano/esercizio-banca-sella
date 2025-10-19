package it.esercizio.banca_sella.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.esercizio.banca_sella.enums.BeneficiaryType;
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
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Boolean isCondoUpgrade;
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String creditorFiscalCode;
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BeneficiaryType beneficiaryType;

    private NaturalPersonBeneficiary naturalPersonBeneficiary;
    private LegalPersonBeneficiary legalPersonBeneficiary;

}
