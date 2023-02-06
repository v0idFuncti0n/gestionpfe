package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.exceptions.EstablishmentException;
import com.gestionpfe.model.Department;
import com.gestionpfe.model.Establishment;
import com.gestionpfe.repository.DepartmentRepository;
import com.gestionpfe.repository.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, EstablishmentRepository establishmentRepository) {
        this.departmentRepository = departmentRepository;
        this.establishmentRepository = establishmentRepository;
    }

    public Department findById(Long departmentId) {
        Optional<Department> branch = departmentRepository.findById(departmentId);
        if(branch.isEmpty()) {
            throw new BranchException(String.format("department id %d not found", departmentId));
        }

        return branch.get();
    }

    public List<Department> findByEstablishment(Long establishmentId) {
        Optional<Establishment> establishmentOptional = establishmentRepository.findById(establishmentId);
        if(establishmentOptional.isEmpty()) {
            throw new EstablishmentException(String.format("establishment id %d not found", establishmentId));
        }
        Establishment establishment = establishmentOptional.get();

        return departmentRepository.findDepartmentByEstablishment(establishment);
    }
}
