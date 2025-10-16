package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fee {

    private String feeCode;
    private String description;
    private Double amount;
}
