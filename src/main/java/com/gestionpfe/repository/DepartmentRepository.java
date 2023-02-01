package com.gestionpfe.repository;

import com.gestionpfe.model.Department;
import com.gestionpfe.model.University;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface DepartmentRepository extends CrudRepository<Department, Long> {

    Optional<Department> findDepartmentByName(String name);
    List<Department> findDepartmentByUniversity(University university);
}
