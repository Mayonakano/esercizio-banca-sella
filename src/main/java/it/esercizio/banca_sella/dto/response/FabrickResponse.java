package it.esercizio.banca_sella.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FabrickResponse<T> {
    private String status;
    private List<FabrickError> errors;
    private T payload;
}