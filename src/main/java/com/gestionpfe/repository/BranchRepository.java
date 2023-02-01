package com.gestionpfe.repository;

import com.gestionpfe.model.Branch;
import com.gestionpfe.model.Department;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BranchRepository extends CrudRepository<Branch, Long> {
    Optional<Branch> findBranchByName(String name);
}
