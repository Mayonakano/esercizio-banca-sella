package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.response.Transaction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

    @Id
    @UniqueElements
    private String transactionId;
    private String operationId;
    private String accountingDate;
    private String valueDate;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tx_type_id")
    private TypeEntity type;
    private Double amount;
    private String currency;
    private String description;

    public TransactionEntity(Transaction transaction) {
        this.transactionId = transaction.getTransactionId();
        this.operationId = transaction.getOperationId();
        this.accountingDate = transaction.getAccountingDate();
        this.valueDate = transaction.getValueDate();
        this.type = new TypeEntity(transaction.getType());
    }
}
