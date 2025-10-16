package it.esercizio.banca_sella.dto.response;

import it.esercizio.banca_sella.dto.common.Amount;
import it.esercizio.banca_sella.dto.common.Creditor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoneyTransferResponse {

    private String moneyTransferId;
    private String status;
    private String direction;
    private Creditor creditor;
    private Creditor debtor;
    private String cro;
    private String uri;
    private String trn;
    private String description;
    private String createdDateTime;
    private String debtorValueDate;
    private String creditorValueDate;
    private Amount amount;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccount;
    private List<Fee> fees;
    private Boolean hasTaxRelief;

}
