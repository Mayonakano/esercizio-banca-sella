package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Balance {

    private String date;
    private Double balance;
    private Double availableBalance;
    private String currency;
}
