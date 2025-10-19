package it.esercizio.banca_sella.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import it.esercizio.banca_sella.dto.TaxRelief;
import it.esercizio.banca_sella.dto.common.Creditor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequest {

    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private Creditor creditor;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "executionDate must be yyyy-MM-dd")
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String executionDate;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String uri;
    @NotBlank
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal amount;
    @NotBlank
    @Size(min = 3, max = 3, message = "currency must be a 3-letter code")
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String currency;
    @JsonProperty("isUrgent")
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isUrgent;
    @JsonProperty("isInstant")
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Boolean isInstant;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String feeType;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String feeAccountId;
    private TaxRelief taxRelief;
}
