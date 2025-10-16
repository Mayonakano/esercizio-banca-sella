package it.esercizio.banca_sella.repository;

import it.esercizio.banca_sella.entity.CreditorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreditorRepository extends JpaRepository<CreditorEntity, Long> {
}
