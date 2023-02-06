package com.gestionpfe.repository;

import com.gestionpfe.model.Establishment;
import com.gestionpfe.model.University;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UniversityRepository extends CrudRepository<University, Long> {
    Optional<University> findUniversityByName(String name);
}
