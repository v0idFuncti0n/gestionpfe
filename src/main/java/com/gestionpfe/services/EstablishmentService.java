package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.exceptions.EstablishmentException;
import com.gestionpfe.exceptions.UniversityException;
import com.gestionpfe.model.Establishment;
import com.gestionpfe.model.University;
import com.gestionpfe.repository.EstablishmentRepository;
import com.gestionpfe.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository, UniversityRepository universityRepository) {
        this.establishmentRepository = establishmentRepository;
        this.universityRepository = universityRepository;
    }

    public Establishment findById(Long establishmentId) {
        Optional<Establishment> establishment = establishmentRepository.findById(establishmentId);
        if (establishment.isEmpty()) {
            throw new BranchException(String.format("establishment id %d not found", establishmentId));
        }

        return establishment.get();
    }

    public List<Establishment> findAll() {
        List<Establishment> establishments = new ArrayList<>();
        establishmentRepository.findAll().forEach(establishments::add);
        return establishments;
    }

    public List<Establishment> findEstablishmentByUniversity(Long universityId) {
        Optional<University> universityOptional = universityRepository.findById(universityId);
        if(universityOptional.isEmpty()) {
            throw new UniversityException(String.format("university id %d not found", universityId));
        }
        University university = universityOptional.get();

        return establishmentRepository.findEstablishmentByUniversity(university);
    }
}
