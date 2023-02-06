package com.gestionpfe.repository;

import com.gestionpfe.model.Establishment;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EstablishmentRepository extends CrudRepository<Establishment, Long> {
    Optional<Establishment> findEstablishmentByName(String name);
}
