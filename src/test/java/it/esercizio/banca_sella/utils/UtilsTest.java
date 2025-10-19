package it.esercizio.banca_sella.utils;

import it.esercizio.banca_sella.dto.NaturalPersonBeneficiary;
import it.esercizio.banca_sella.dto.TaxRelief;
import it.esercizio.banca_sella.dto.common.Account;
import it.esercizio.banca_sella.dto.common.Creditor;
import it.esercizio.banca_sella.dto.request.MoneyTransferRequest;
import it.esercizio.banca_sella.enums.BeneficiaryType;

import java.math.BigDecimal;

public class UtilsTest {

    public static MoneyTransferRequest moneyTransferRequestMocked() {
        MoneyTransferRequest request = new MoneyTransferRequest();
        Creditor creditor = new Creditor();
        Account account = new Account();
        account.setAccountCode("xxxxxxxxxx");
        creditor.setName("John Doe");
        creditor.setAccount(account);
        request.setCreditor(creditor);
        request.setExecutionDate("2025-10-20");
        request.setDescription("test");
        request.setAmount(new BigDecimal(100));
        request.setCurrency("EUR");
        TaxRelief taxRelief = new TaxRelief();
        taxRelief.setIsCondoUpgrade(true);
        taxRelief.setCreditorFiscalCode("xxxxxxxxxx");
        taxRelief.setBeneficiaryType(BeneficiaryType.NATURAL_PERSON);
        taxRelief.setNaturalPersonBeneficiary(new NaturalPersonBeneficiary("XXXXXX95B31R182V"));
        request.setTaxRelief(taxRelief);
        return request;
    }
}
