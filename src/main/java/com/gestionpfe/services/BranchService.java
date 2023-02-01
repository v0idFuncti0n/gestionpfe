package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.model.Branch;
import com.gestionpfe.model.Department;
import com.gestionpfe.repository.BranchRepository;
import com.gestionpfe.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public BranchService(BranchRepository branchRepository, DepartmentRepository departmentRepository) {
        this.branchRepository = branchRepository;
        this.departmentRepository = departmentRepository;
    }

    public Branch findById(Long branchId) {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if(branch.isEmpty()) {
            throw new BranchException(String.format("branch id %d not found", branchId));
        }

        return branch.get();
    }

    public List<Branch> findByDepartment(Long departmentId) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);
        if(departmentOptional.isEmpty()) {
            throw new BranchException(String.format("department id %d not found", departmentId));
        }

        Department department = departmentOptional.get();
        return department.getBranches();
    }
}
