package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.model.Establishment;
import com.gestionpfe.model.University;
import com.gestionpfe.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UniversityService {

    private final UniversityRepository universityRepository;

    @Autowired
    public UniversityService(UniversityRepository universityRepository) {
        this.universityRepository = universityRepository;
    }

    public University findById(Long universityId) {
        Optional<University> university = universityRepository.findById(universityId);
        if (university.isEmpty()) {
            throw new BranchException(String.format("university id %d not found", universityId));
        }

        return university.get();
    }

    public List<University> findAll() {
        List<University> universities = new ArrayList<>();
        universityRepository.findAll().forEach(universities::add);
        return universities;
    }
}
