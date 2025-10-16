package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabrickError {
    private String code;
    private String description;
    private String params;
}