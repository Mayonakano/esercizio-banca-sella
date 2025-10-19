package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private String date;
    private BigDecimal balance;
    private BigDecimal availableBalance;
    private String currency;
}
