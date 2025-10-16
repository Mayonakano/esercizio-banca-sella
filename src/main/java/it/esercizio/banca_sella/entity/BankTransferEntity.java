package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.response.MoneyTransferResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "bank_transfer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankTransferEntity {

    @Id
    private String moneyTransferId;
    private String status;
    private String direction;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creditor_id")
    private CreditorEntity creditor;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "debtor_id")
    private CreditorEntity debtor;
    private String cro;
    private String uri;
    private String trn;
    private String description;
    private String createdDateTime;
    private String debtorValueDate;
    private String creditorValueDate;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "amount_id")
    private AmountEntity amount;
    private Boolean isUrgent;
    private Boolean isInstant;
    private String feeType;
    private String feeAccount;
    @OneToMany(mappedBy = "bankTransfer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeEntity> fees = new ArrayList<>();
    private Boolean hasTaxRelief;

    public BankTransferEntity(MoneyTransferResponse result) {
        this.moneyTransferId = result.getMoneyTransferId();
        this.status = result.getStatus();
        this.direction = result.getDirection();
        this.creditor = new CreditorEntity(result.getCreditor());
        this.debtor = new CreditorEntity(result.getDebtor());
        this.cro = result.getCro();
        this.uri = result.getUri();
        this.trn = result.getTrn();
        this.description = result.getDescription();
        this.createdDateTime = result.getCreatedDateTime();
        this.debtorValueDate = result.getDebtorValueDate();
        this.creditorValueDate = result.getCreditorValueDate();
        this.amount = new AmountEntity(result.getAmount());
        this.isUrgent = result.getIsUrgent();
        this.isInstant = result.getIsInstant();
        if (result.getFees() != null) {
            this.fees = result.getFees().stream().map(f -> new FeeEntity(f, this)).collect(Collectors.toList());
        }
        this.feeType = result.getFeeType();
        this.feeAccount = result.getFeeAccount();
        this.hasTaxRelief = result.getHasTaxRelief();

    }
}
