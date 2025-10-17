package it.esercizio.banca_sella.service;

import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.*;
import it.esercizio.banca_sella.entity.BankTransferEntity;
import it.esercizio.banca_sella.feign.FabrickClient;
import it.esercizio.banca_sella.repository.BankTransferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class FabrickService {

    @Value("${fabrick_client.accountId}")
    private String accountId;

    @Autowired
    private BankTransferRepository bankTransferRepository;

    @Autowired
    private FabrickClient fabrickClient;

    public Double getAccountBalance() {
        log.info("Getting account balance on service");
        try {
            log.info("Checking balance with fabrick api with accountId {}", accountId);
            FabrickResponse<Balance> response = fabrickClient.getBalance(accountId);
            if (response == null || response.getPayload() == null) {
                throw new Exception("Fabrick API returned null response");
            }
            Balance balance = response.getPayload();
            log.info("Balance payload: {}", balance);
            return balance.getBalance() != null ? balance.getBalance() : 0.0;
        } catch (Exception e) {
            log.error("Wrong account or service unavailable", e);
            return 0.0;
        }
    }

    public MoneyTransferResponse getMoneyTransfers(MoneyTransferRequest moneyTransferRequest) {
        log.info("Getting money transfers from fabrick api");
        log.info("Calling fabrick api with request {}", moneyTransferRequest);
        // Use configured accountId for path variable to avoid empty ID causing double slash in URL
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("Configured accountId is missing");
        }
        MoneyTransferResponse result = fabrickClient.getMoneyTransfers(accountId, moneyTransferRequest);
        log.info("Money transfers: {}", result);
        BankTransferEntity bankTransferEntity = new BankTransferEntity(result);
        log.info("this transaction are going saved: {}", bankTransferEntity);
        bankTransferRepository.save(bankTransferEntity);
        return result;
    }

    public List<Transaction> getTransactions(String dateFrom, String dateTo) {
        try {
            log.info("Getting transactions from fabrick api for accountId {}", accountId);
            FabrickResponse<TransactionsPayload> response = fabrickClient.getTransactions(accountId, dateFrom, dateTo);
            if (response == null || response.getPayload() == null) {
                throw new NullPointerException("Fabrick transactions response or payload is null");
            }
            TransactionsPayload transactionsPayload = response.getPayload();
            List<Transaction> result = transactionsPayload.getList();
            log.info("Transactions returned: {}", result);
            return result;
        } catch (NullPointerException e) {
            log.error(e.getMessage());
            return new ArrayList<>();
        }
    }

}
