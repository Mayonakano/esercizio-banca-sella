package it.esercizio.banca_sella.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private Double debtorAmount;
    private String debtorCurrency;
    private Double creditorAmount;
    private String creditorCurrency;
    private String creditorCurrencyDate;
    private Float exchangeRate;
}
