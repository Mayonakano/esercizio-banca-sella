package it.esercizio.banca_sella.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.dto.response.MoneyTransferResponse;
import it.esercizio.banca_sella.dto.response.Transaction;
import it.esercizio.banca_sella.service.FabrickService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/operation")
@Slf4j
@Validated
@Tag(name = "Operations", description = "Operations for balance, bank transfers and get all transactions")
@RequiredArgsConstructor
public class OperationsController {

    private final FabrickService fabrickService;

    @GetMapping("/accountBalance")
    @Operation(
            summary = "Get account balance",
            description = "Returns the current balance for the configured account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Balance retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BigDecimal.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Balance not found", content = @Content)
            }
    )
    public ResponseEntity<BigDecimal> accountBalance() {
        log.info("Getting account balance on controller");
        return new ResponseEntity<>(fabrickService.accountBalance(), HttpStatus.OK);
    }

    @PostMapping("/transaction")
    @Operation(
            summary = "Create a money transfer",
            description = "Creates a new money transfer for the configured account",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transfer created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = MoneyTransferResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Related entity not found", content = @Content)
            }
    )
    public ResponseEntity<MoneyTransferResponse> transaction(@Valid @RequestBody MoneyTransferRequest moneyTransferRequest) {
        log.info("Creating transaction on controller");
        return new ResponseEntity<>(fabrickService.moneyTransfers(moneyTransferRequest), HttpStatus.OK);
    }

    @GetMapping("/allTransaction")
    @Operation(
            summary = "Get transactions by accounting date range",
            description = "Returns transactions within the specified accounting date range in yyyy-MM-dd format",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transactions retrieved",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Transaction.class))),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No transactions found", content = @Content)
            }
    )
    public ResponseEntity<List<Transaction>> transactionsWithDate(
            @Parameter(description = "Start accounting date yyyy-MM-dd", required = true)
            @RequestParam(value = "fromAccountingDate") @NotBlank String dateFrom,
            @Parameter(description = "End accounting date yyyy-MM-dd", required = true)
            @RequestParam(value = "toAccountingDate") @NotBlank String dateTo) {
        log.info("Getting transactions from dateFrom {} to dateTo {}", dateFrom, dateTo);
        return new ResponseEntity<>(fabrickService.transactions(dateFrom, dateTo), HttpStatus.OK);
    }
}
