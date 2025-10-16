package it.esercizio.banca_sella.dto.common;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    private String accountId;
    @NotNull
    private String accountCode;
    @Size(max = 27)
    private String iban;
    private String abiCode;
    private String bicCode;
    private String cabCode;
    private String countryCode;
    private String internationalCin;
    private String nationalCin;
    private String account;
    private String alias;
    private String productName;
    private String holderName;
    private String activateDate;
    private String currency;
}
