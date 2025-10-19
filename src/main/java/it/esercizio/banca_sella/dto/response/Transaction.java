package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    private String transactionId;
    private String operationId;
    private String accountingDate;
    private String valueDate;
    private Type type;
    private BigDecimal amount;
    private String currency;
    private String description;
}
