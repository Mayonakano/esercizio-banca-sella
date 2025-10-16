package it.esercizio.banca_sella.repository;

import it.esercizio.banca_sella.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<AccountEntity, Long> {
}
