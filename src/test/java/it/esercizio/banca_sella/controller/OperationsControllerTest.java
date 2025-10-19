package it.esercizio.banca_sella.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.MoneyTransferResponse;
import it.esercizio.banca_sella.dto.response.Transaction;
import it.esercizio.banca_sella.service.FabrickService;
import it.esercizio.banca_sella.utils.UtilsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OperationsController.class)
class OperationsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FabrickService fabrickService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /operation/accountBalance returns balance")
    void accountBalance() throws Exception {
        when(fabrickService.accountBalance()).thenReturn(BigDecimal.valueOf(100.0));
        mockMvc.perform(get("/operation/accountBalance"))
                .andExpect(status().isOk())
                .andExpect(content().string(is("100.0")));
    }

    @Test
    @DisplayName("GET /operation/accountBalance returns 400 on IllegalArgumentException")
    void accountBalance_illegalArgument_returns400() throws Exception {
        when(fabrickService.accountBalance()).thenThrow(new IllegalArgumentException("bad request"));
        mockMvc.perform(get("/operation/accountBalance"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /operation/accountBalance returns 404 on NullPointerException")
    void accountBalance_nullPointer_returns404() throws Exception {
        when(fabrickService.accountBalance()).thenThrow(new NullPointerException("not found"));
        mockMvc.perform(get("/operation/accountBalance"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /operation/transaction returns response")
    void transaction() throws Exception {
        MoneyTransferResponse response = new MoneyTransferResponse();
        MoneyTransferRequest request = UtilsTest.moneyTransferRequestMocked();
        // Fix invalid CF in utility to satisfy @Pattern
        request.getTaxRelief().setCreditorFiscalCode("RSSMRA85T10A562S");

        when(fabrickService.moneyTransfers(any(MoneyTransferRequest.class))).thenReturn(response);

        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/operation/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /operation/transaction returns 400 on IllegalArgumentException from service")
    void transaction_illegalArgument_returns400() throws Exception {
        MoneyTransferRequest request = UtilsTest.moneyTransferRequestMocked();
        request.getTaxRelief().setCreditorFiscalCode("RSSMRA85T10A562S");
        when(fabrickService.moneyTransfers(any(MoneyTransferRequest.class)))
                .thenThrow(new IllegalArgumentException("bad req"));

        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/operation/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /operation/transaction returns 404 on NullPointerException from service")
    void transaction_nullPointer_returns404() throws Exception {
        MoneyTransferRequest request = UtilsTest.moneyTransferRequestMocked();
        request.getTaxRelief().setCreditorFiscalCode("RSSMRA85T10A562S");
        when(fabrickService.moneyTransfers(any(MoneyTransferRequest.class)))
                .thenThrow(new NullPointerException("missing"));

        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/operation/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /operation/transaction returns 400 on validation errors")
    void transaction_validation_returns400() throws Exception {
        // Empty body violates @Valid constraints, should not hit service
        mockMvc.perform(post("/operation/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(fabrickService);
    }

    @Test
    @DisplayName("GET /operation/getAllTransaction returns list")
    void getAllTransactions() throws Exception {
        when(fabrickService.transactions(eq("2020-01-01"), eq("2020-01-31")))
                .thenReturn(List.of(new Transaction()));

        mockMvc.perform(get("/operation/allTransaction")
                        .param("fromAccountingDate", "2020-01-01")
                        .param("toAccountingDate", "2020-01-31"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /operation/allTransaction returns 400 on IllegalArgumentException")
    void getAllTransactions_illegalArgument_returns400() throws Exception {
        when(fabrickService.transactions(eq("2020-01-01"), eq("2020-01-31")))
                .thenThrow(new IllegalArgumentException("bad params"));

        mockMvc.perform(get("/operation/allTransaction")
                        .param("fromAccountingDate", "2020-01-01")
                        .param("toAccountingDate", "2020-01-31"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /operation/allTransaction returns 404 on NullPointerException")
    void getAllTransactions_nullPointer_returns404() throws Exception {
        when(fabrickService.transactions(eq("2020-01-01"), eq("2020-01-31")))
                .thenThrow(new NullPointerException("none"));

        mockMvc.perform(get("/operation/allTransaction")
                        .param("fromAccountingDate", "2020-01-01")
                        .param("toAccountingDate", "2020-01-31"))
                .andExpect(status().isNotFound());
    }



}
