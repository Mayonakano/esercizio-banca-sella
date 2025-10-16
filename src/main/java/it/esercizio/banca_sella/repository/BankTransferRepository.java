package it.esercizio.banca_sella.repository;

import it.esercizio.banca_sella.entity.BankTransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransferEntity, Integer> {
}
