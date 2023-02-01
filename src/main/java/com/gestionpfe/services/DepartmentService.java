package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.exceptions.UniversityException;
import com.gestionpfe.model.Department;
import com.gestionpfe.model.University;
import com.gestionpfe.repository.DepartmentRepository;
import com.gestionpfe.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, UniversityRepository universityRepository) {
        this.departmentRepository = departmentRepository;
        this.universityRepository = universityRepository;
    }

    public Department findById(Long departmentId) {
        Optional<Department> branch = departmentRepository.findById(departmentId);
        if(branch.isEmpty()) {
            throw new BranchException(String.format("department id %d not found", departmentId));
        }

        return branch.get();
    }

    public List<Department> findByUniversity(Long universityId) {
        Optional<University> universityOptional = universityRepository.findById(universityId);
        if(universityOptional.isEmpty()) {
            throw new UniversityException(String.format("university id %d not found", universityId));
        }
        University university = universityOptional.get();

        List<Department> departments = departmentRepository.findDepartmentByUniversity(university);

        return departments;
    }
}
