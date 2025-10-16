package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.response.Fee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fee")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String feeCode;
    private String description;
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_transfer_id")
    private BankTransferEntity bankTransfer;

    public FeeEntity(Fee fee, BankTransferEntity bankTransfer) {
        this.feeCode = fee.getFeeCode();
        this.description = fee.getDescription();
        this.amount = fee.getAmount();
        this.bankTransfer = bankTransfer;
    }
}
