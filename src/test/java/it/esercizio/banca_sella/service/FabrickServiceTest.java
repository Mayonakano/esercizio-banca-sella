package it.esercizio.banca_sella.service;


import it.esercizio.banca_sella.dto.common.Account;
import it.esercizio.banca_sella.dto.common.Address;
import it.esercizio.banca_sella.dto.common.Amount;
import it.esercizio.banca_sella.dto.common.Creditor;
import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.*;
import it.esercizio.banca_sella.entity.BankTransferEntity;
import it.esercizio.banca_sella.feign.FabrickClient;
import it.esercizio.banca_sella.repository.BankTransferRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FabrickServiceTest {

    @Mock
    private BankTransferRepository bankTransferRepository;

    @Mock
    private FabrickClient fabrickClient;

    @InjectMocks
    private FabrickService fabrickService;

    private final String accountId = "123456";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(fabrickService, "accountId", accountId);
    }

    @Test
    @DisplayName("accountBalance returns payload balance when present")
    void accountBalance_ok() {
        Balance balance = new Balance();
        balance.setBalance(BigDecimal.valueOf(42.5));
        FabrickResponse<Balance> response = new FabrickResponse<>();
        response.setPayload(balance);
        when(fabrickClient.getBalance(accountId)).thenReturn(response);

        BigDecimal result = fabrickService.accountBalance();

        assertThat(result).isEqualTo(BigDecimal.valueOf(42.5));
        verify(fabrickClient, times(1)).getBalance(accountId);
    }

    @Test
    @DisplayName("accountBalance throws when response or payload is null")
    void accountBalance_nullPayload_throws() {
        when(fabrickClient.getBalance(accountId)).thenReturn(null);
        assertThatThrownBy(() -> fabrickService.accountBalance())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Fabrick API returned null response");

        FabrickResponse<Balance> response = new FabrickResponse<>();
        response.setPayload(null);
        when(fabrickClient.getBalance(accountId)).thenReturn(response);
        assertThatThrownBy(() -> fabrickService.accountBalance())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Fabrick API returned null response");
    }

    @Test
    @DisplayName("getMoneyTransfers calls client, maps fees, and persists entity")
    void getMoneyTransfers_ok() {
        MoneyTransferRequest req = new MoneyTransferRequest();
        MoneyTransferResponse resp = new MoneyTransferResponse();
        // Minimal nested objects required by BankTransferEntity constructor
        Creditor creditor = new Creditor();
        creditor.setName("John Doe");
        Account account = new Account();
        account.setAccountCode("12345");
        account.setIban("IT60X0542811101000000123456");
        creditor.setAccount(account);
        Address address = new Address();
        address.setAddress("Via Roma 1");
        address.setCity("Milano");
        address.setCountryCode("IT");
        creditor.setAddress(address);
        resp.setCreditor(creditor);
        resp.setDebtor(creditor);
        Amount amount = new Amount();
        amount.setCreditorAmount(BigDecimal.valueOf(10.0));
        amount.setCreditorCurrency("EUR");
        resp.setAmount(amount);
        // Add a fees section to fully cover BankTransferEntity mapping
        Fee fee = new Fee();
        fee.setFeeCode("FEE001");
        fee.setDescription("Transfer commission");
        fee.setAmount(BigDecimal.valueOf(0.99));
        resp.setFees(List.of(fee));

        when(fabrickClient.getMoneyTransfers(accountId, req)).thenReturn(resp);

        MoneyTransferResponse result = fabrickService.moneyTransfers(req);

        assertThat(result).isSameAs(resp);
        ArgumentCaptor<BankTransferEntity> captor = ArgumentCaptor.forClass(BankTransferEntity.class);
        verify(bankTransferRepository).save(captor.capture());
        BankTransferEntity saved = captor.getValue();
        assertThat(saved).isNotNull();
        // Assert fees are mapped into FeeEntity with back-reference set
        assertThat(saved.getFees()).hasSize(1);
        assertThat(saved.getFees().get(0).getFeeCode()).isEqualTo("FEE001");
        assertThat(saved.getFees().get(0).getDescription()).isEqualTo("Transfer commission");
        assertThat(saved.getFees().get(0).getAmount()).isEqualByComparingTo("0.99");
        assertThat(saved.getFees().get(0).getBankTransfer()).isSameAs(saved);
        verify(fabrickClient).getMoneyTransfers(accountId, req);
    }

    @Test
    @DisplayName("getMoneyTransfers calls client, null maps fees, and persists entity")
    void getMoneyTransfers_ok_with_null_fee() {
        MoneyTransferRequest req = new MoneyTransferRequest();
        MoneyTransferResponse resp = new MoneyTransferResponse();
        // Minimal nested objects required by BankTransferEntity constructor
        Creditor creditor = new Creditor();
        creditor.setName("John Doe");
        Account account = new Account();
        account.setAccountCode("12345");
        account.setIban("IT60X0542811101000000123456");
        creditor.setAccount(account);
        Address address = new Address();
        address.setAddress("Via Roma 1");
        address.setCity("Milano");
        address.setCountryCode("IT");
        creditor.setAddress(address);
        resp.setCreditor(creditor);
        resp.setDebtor(creditor);
        Amount amount = new Amount();
        amount.setCreditorAmount(BigDecimal.valueOf(10.0));
        amount.setCreditorCurrency("EUR");
        resp.setAmount(amount);
        resp.setFees(null);

        when(fabrickClient.getMoneyTransfers(accountId, req)).thenReturn(resp);

        MoneyTransferResponse result = fabrickService.moneyTransfers(req);

        assertThat(result).isSameAs(resp);
        ArgumentCaptor<BankTransferEntity> captor = ArgumentCaptor.forClass(BankTransferEntity.class);
        verify(bankTransferRepository).save(captor.capture());
        BankTransferEntity saved = captor.getValue();
        assertThat(saved).isNotNull();
        assertThat(saved.getFees()).isEmpty();
        verify(fabrickClient).getMoneyTransfers(accountId, req);
    }


    @Test
    @DisplayName("getMoneyTransfers calls client, no maps fees, and persists entity")
    void getMoneyTransfers_ok_without_fee() {
        MoneyTransferRequest req = new MoneyTransferRequest();
        MoneyTransferResponse resp = new MoneyTransferResponse();
        // Minimal nested objects required by BankTransferEntity constructor
        Creditor creditor = new Creditor();
        creditor.setName("John Doe");
        Account account = new Account();
        account.setAccountCode("12345");
        account.setIban("IT60X0542811101000000123456");
        creditor.setAccount(account);
        Address address = new Address();
        address.setAddress("Via Roma 1");
        address.setCity("Milano");
        address.setCountryCode("IT");
        creditor.setAddress(address);
        resp.setCreditor(creditor);
        resp.setDebtor(creditor);
        Amount amount = new Amount();
        amount.setCreditorAmount(BigDecimal.valueOf(10.0));
        amount.setCreditorCurrency("EUR");
        resp.setAmount(amount);
        resp.setFees(new ArrayList<>());

        when(fabrickClient.getMoneyTransfers(accountId, req)).thenReturn(resp);

        MoneyTransferResponse result = fabrickService.moneyTransfers(req);

        assertThat(result).isSameAs(resp);
        ArgumentCaptor<BankTransferEntity> captor = ArgumentCaptor.forClass(BankTransferEntity.class);
        verify(bankTransferRepository).save(captor.capture());
        BankTransferEntity saved = captor.getValue();
        assertThat(saved).isNotNull();
        assertThat(saved.getFees()).isEmpty();
        verify(fabrickClient).getMoneyTransfers(accountId, req);
    }

    @Test
    @DisplayName("getMoneyTransfers throws when configured accountId is missing")
    void getMoneyTransfers_missingAccountId_throws() {
        ReflectionTestUtils.setField(fabrickService, "accountId", " ");
        MoneyTransferRequest req = new MoneyTransferRequest();
        assertThatThrownBy(() -> fabrickService.moneyTransfers(req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("accountId is missing");
        verifyNoInteractions(bankTransferRepository);
        verifyNoInteractions(fabrickClient);
    }

    @Test
    @DisplayName("getTransactions returns transactions list from payload")
    void getTransactions_ok() {
        List<Transaction> txs = new ArrayList<>();
        txs.add(new Transaction());
        TransactionsPayload payload = new TransactionsPayload();
        payload.setList(txs);
        FabrickResponse<TransactionsPayload> response = new FabrickResponse<>();
        response.setPayload(payload);

        when(fabrickClient.getTransactions(accountId, "2020-01-01", "2020-01-31")).thenReturn(response);

        List<Transaction> result = fabrickService.transactions("2020-01-01", "2020-01-31");
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("getTransactions throws when payload is null")
    void getTransactions_nullPayload_throws() {
        FabrickResponse<TransactionsPayload> response = new FabrickResponse<>();
        response.setPayload(null);
        when(fabrickClient.getTransactions(accountId, "2020-01-01", "2020-01-31")).thenReturn(response);

        assertThatThrownBy(() -> fabrickService.transactions("2020-01-01", "2020-01-31"))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Fabrick transactions response or payload is null");
    }

    @Test
    @DisplayName("getTransactions throws IllegalArgumentException when dates or accountId invalid")
    void getTransactions_invalidParams_throws() {
        // Blank dateFrom
        assertThatThrownBy(() -> fabrickService.transactions("", "2020-01-31"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalid dateFrom or dateTo");

        // Blank dateTo
        assertThatThrownBy(() -> fabrickService.transactions("2020-01-01", ""))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("invalid dateFrom or dateTo");

        // Blank accountId
        ReflectionTestUtils.setField(fabrickService, "accountId", " ");
        assertThatThrownBy(() -> fabrickService.transactions("2020-01-01", "2020-01-31"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("accountId is missing");

        // Ensure no downstream calls are made when inputs are invalid
        verifyNoInteractions(fabrickClient);
    }
}

