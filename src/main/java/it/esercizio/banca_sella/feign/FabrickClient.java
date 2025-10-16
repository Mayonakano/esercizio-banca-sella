package it.esercizio.banca_sella.feign;

import it.esercizio.banca_sella.dto.response.Balance;
import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.FabrickResponse;
import it.esercizio.banca_sella.dto.response.MoneyTransferResponse;
import it.esercizio.banca_sella.dto.response.Transaction;
import it.esercizio.banca_sella.dto.response.TransactionsPayload;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "fabrick-client",
        url = "${fabrick_client.url}",
        configuration = FeignConfig.class)
public interface FabrickClient {

    @GetMapping("/{accountId}/balance")
    FabrickResponse<Balance> getBalance(@PathVariable String accountId);

    @PostMapping(value = "/{accountId}/payments/money-transfers")
    MoneyTransferResponse getMoneyTransfers(@PathVariable("accountId") String accountId, @RequestBody MoneyTransferRequest moneyTransferRequest);

    @GetMapping("/{accountId}/transactions")
    FabrickResponse<TransactionsPayload> getTransactions(@PathVariable("accountId") String accountId,
                                  @RequestParam String fromAccountingDate, @RequestParam String toAccountingDate);

}
