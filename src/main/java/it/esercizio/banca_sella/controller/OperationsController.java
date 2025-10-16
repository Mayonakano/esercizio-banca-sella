package it.esercizio.banca_sella.controller;

import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.MoneyTransferResponse;
import it.esercizio.banca_sella.dto.response.Transaction;
import it.esercizio.banca_sella.service.FabrickService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/operation")
@Slf4j
public class OperationsController {

    @Autowired
    private FabrickService fabrickService;

    @GetMapping("/getAccountBalance")
    public ResponseEntity<Double> getAccountBalance() {
        log.info("Getting account balance on controller");
        return new ResponseEntity<>(fabrickService.getAccountBalance(), HttpStatus.OK);
    }

    @PostMapping("/transaction")
    public ResponseEntity<MoneyTransferResponse> transaction( @RequestBody MoneyTransferRequest moneyTransferRequest) {
        log.info("Creating transaction on controller");
        return new ResponseEntity<>(fabrickService.getMoneyTransfers(moneyTransferRequest), HttpStatus.OK);
    }

    @GetMapping("/getAllTransaction")
    public ResponseEntity<List<Transaction>> getAllTransactionsWithDate(
            @RequestParam(value = "fromAccountingDate") String dateFrom,@RequestParam(value = "toAccountingDate") String dateTo) {
        return new ResponseEntity<>(fabrickService.getTransactions(dateFrom, dateTo), HttpStatus.OK);
    }
}
