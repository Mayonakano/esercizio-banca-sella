package it.esercizio.banca_sella.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.esercizio.banca_sella.dto.TaxRelief;
import it.esercizio.banca_sella.dto.common.Creditor;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferRequest {

    @NotNull
    
    private Creditor creditor;
    @NotBlank
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "executionDate must be yyyy-MM-dd")
    private String executionDate;
    private String uri;
    @NotBlank
    private String description;
    @NotNull
    @Positive(message = "amount must be > 0")
    private Double amount;
    @NotBlank
    @Size(min = 3, max = 3, message = "currency must be a 3-letter code")
    private String currency;
    @JsonProperty("isUrgent")
    private Boolean isUrgent;
    @JsonProperty("isInstant")
    private Boolean isInstant;
    private String feeType;
    private String feeAccountId;
    @NotNull
    
    private TaxRelief taxRelief;

    @AssertTrue(message = "Request cannot be both urgent and instant")
    public boolean isUrgentInstantValid() {
        if (isUrgent == null || isInstant == null) return true;
        return !(Boolean.TRUE.equals(isUrgent) && Boolean.TRUE.equals(isInstant));
    }
}
