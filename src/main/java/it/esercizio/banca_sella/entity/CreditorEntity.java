package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.common.Creditor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "creditor")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_account_id")
    private AccountEntity account;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    public CreditorEntity(Creditor creditor) {
        this.name = creditor.getName();
        this.account = new AccountEntity(creditor.getAccount());
        this.address = new AddressEntity(creditor.getAddress());
    }
}
