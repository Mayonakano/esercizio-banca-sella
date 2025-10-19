package it.esercizio.banca_sella.dto.common;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String accountId;
    @NotNull
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountCode;
    @Size(max = 27)
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String iban;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String abiCode;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String bicCode;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String cabCode;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String countryCode;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String internationalCin;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String nationalCin;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String account;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String alias;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String productName;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String holderName;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String activateDate;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String currency;
}
