package it.esercizio.banca_sella.service;

import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.*;
import it.esercizio.banca_sella.entity.BankTransferEntity;
import it.esercizio.banca_sella.feign.FabrickClient;
import it.esercizio.banca_sella.repository.BankTransferRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FabrickService {

    @Value("${fabrick_client.accountId}")
    private String accountId;

    private final BankTransferRepository bankTransferRepository;

    private final FabrickClient fabrickClient;

    public BigDecimal accountBalance() {
        log.info("Getting account balance on service");
        log.info("Checking balance with fabrick api with accountId {}", accountId);
        FabrickResponse<Balance> response = fabrickClient.getBalance(accountId);
        if (response == null || response.getPayload() == null) {
            throw new NullPointerException("Fabrick API returned null response");
        }
        Balance balance = response.getPayload();
        log.info("Balance payload: {}", balance);
        return balance.getBalance() != null ? balance.getBalance() : BigDecimal.valueOf(0.0);
    }

    public MoneyTransferResponse moneyTransfers(MoneyTransferRequest moneyTransferRequest) {
        log.info("Getting money transfers from fabrick api");
        log.info("Calling fabrick api with request {}", moneyTransferRequest);
        if (accountId == null || accountId.isBlank() || moneyTransferRequest == null) {
            throw new IllegalArgumentException("Configured accountId is missing or invalid body request");
        }
        MoneyTransferResponse result = fabrickClient.getMoneyTransfers(accountId, moneyTransferRequest);
        log.info("Money transfers: {}", result);
        BankTransferEntity bankTransferEntity = new BankTransferEntity(result);
        log.info("this transaction are going saved: {}", bankTransferEntity);
        bankTransferRepository.save(bankTransferEntity);
        return result;
    }

    public List<Transaction> transactions(String dateFrom, String dateTo) {
        log.info("Getting transactions from fabrick api for accountId {}", accountId);
        if(dateFrom == null || dateFrom.isBlank() || dateTo == null || dateTo.isBlank() || accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Configured accountId is missing or invalid dateFrom or dateTo");
        }
        FabrickResponse<TransactionsPayload> response = fabrickClient.getTransactions(accountId, dateFrom, dateTo);
        if (response == null || response.getPayload() == null) {
            throw new NullPointerException("Fabrick transactions response or payload is null");
        }
        TransactionsPayload transactionsPayload = response.getPayload();
        List<Transaction> result = transactionsPayload.getList();
        log.info("Transactions returned: {}", result);
        return result;
    }

}
