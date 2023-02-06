package com.gestionpfe.services;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.model.Establishment;
import com.gestionpfe.repository.EstablishmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EstablishmentService {

    private final EstablishmentRepository establishmentRepository;

    @Autowired
    public EstablishmentService(EstablishmentRepository establishmentRepository) {
        this.establishmentRepository = establishmentRepository;
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
}
