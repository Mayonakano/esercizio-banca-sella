package it.esercizio.banca_sella.dto.response;

import it.esercizio.banca_sella.dto.common.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAccounts {

    private List<Account> list;
}
