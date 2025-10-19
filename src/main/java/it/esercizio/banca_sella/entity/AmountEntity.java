package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.common.Amount;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "amount")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal debtorAmount;
    private String debtorCurrency;
    private BigDecimal creditorAmount;
    private String creditorCurrency;
    private String creditorCurrencyDate;
    private Float exchangeRate;

    @Generated
    public AmountEntity(Amount amount) {
        this.debtorAmount = amount.getDebtorAmount();
        this.debtorCurrency = amount.getDebtorCurrency();
        this.creditorAmount = amount.getCreditorAmount();
        this.creditorCurrency = amount.getCreditorCurrency();
        this.creditorCurrencyDate = amount.getCreditorCurrencyDate();
        this.exchangeRate = amount.getExchangeRate();
    }
}
