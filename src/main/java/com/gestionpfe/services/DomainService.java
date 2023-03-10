package com.gestionpfe.services;

import com.gestionpfe.exceptions.DomainException;
import com.gestionpfe.model.Domain;
import com.gestionpfe.model.requests.DomainRequest;
import com.gestionpfe.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DomainService {

    private final DomainRepository domainRepository;

    @Autowired
    public DomainService(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    public Domain save(DomainRequest request) {
        Domain domain = new Domain(request.getName(), request.getAppUserRole());

        boolean isDomainExists = domainRepository.findByName(domain.getName()).isPresent();

        if(isDomainExists) {
            throw new DomainException(String.format("Domain name exists %s", domain.getName()));
        }

        domainRepository.save(domain);
        return domain;
    }

    public List<Domain> findAll() {
        List<Domain> domains = new ArrayList<>();
        domainRepository.findAll().forEach(domains::add);
        return domains;
    }
}
