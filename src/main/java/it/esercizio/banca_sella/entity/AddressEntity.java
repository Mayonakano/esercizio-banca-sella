package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.common.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "address")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String city;
    private String countryCode;

    public AddressEntity(Address address) {
        this.address = address.getAddress();
        this.city = address.getCity();
        this.countryCode = address.getCountryCode();
    }
}
