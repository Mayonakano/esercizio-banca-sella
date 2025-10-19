package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.common.Account;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "account")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    private String accountId;
    private String accountCode;
    @Size(max = 27)
    @NotNull
    private String iban;
    private String abiCode;
    private String bicCode;
    private String cabCode;
    private String countryCode;
    private String internationalCin;
    private String nationalCin;
    private String account;
    private String alias;
    private String productName;
    private String holderName;
    private String activateDate;
    private String currency;

    @Generated
    public AccountEntity(Account account) {
        this.accountId = account.getAccountId();
        this.accountCode = account.getAccountCode();
        this.iban = account.getIban();
        this.abiCode = account.getAbiCode();
        this.bicCode = account.getBicCode();
        this.cabCode = account.getCabCode();
        this.countryCode = account.getCountryCode();
        this.internationalCin = account.getInternationalCin();
        this.nationalCin = account.getNationalCin();
        this.account = account.getAccount();
        this.alias = account.getAlias();
        this.productName = account.getProductName();
        this.holderName = account.getHolderName();
        this.activateDate = account.getActivateDate();
        this.currency = account.getCurrency();
    }
}
