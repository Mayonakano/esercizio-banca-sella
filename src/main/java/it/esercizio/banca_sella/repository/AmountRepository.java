package it.esercizio.banca_sella.repository;

import it.esercizio.banca_sella.entity.AmountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmountRepository extends JpaRepository<AmountEntity, Long> {
}
