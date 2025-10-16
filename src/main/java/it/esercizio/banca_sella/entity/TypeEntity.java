package it.esercizio.banca_sella.entity;

import it.esercizio.banca_sella.dto.response.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tx_type")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type_enumeration")
    private String enumeration;

    @Column(name = "type_value")
    private String value;

    public TypeEntity(Type type) {
        this.enumeration = type.getEnumeration();
        this.value = type.getValue();
    }
}
