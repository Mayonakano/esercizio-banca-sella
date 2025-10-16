package it.esercizio.banca_sella.repository;

import it.esercizio.banca_sella.entity.TypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends JpaRepository<TypeEntity, Long> {

}
