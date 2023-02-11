package com.gestionpfe.controller;

import com.gestionpfe.model.Domain;
import com.gestionpfe.model.requests.DomainRequest;
import com.gestionpfe.model.responses.DomainResponse;
import com.gestionpfe.services.DomainService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/domain")
public class DomainController {

    private final DomainService domainService;

    @Autowired
    public DomainController(DomainService domainService) {
        this.domainService = domainService;
    }

    @PostMapping
    public ResponseEntity<DomainResponse> saveDomain(@Valid @RequestBody DomainRequest request) {
        DomainResponse domainResponse = new DomainResponse();
        Domain domain = domainService.save(request);
        BeanUtils.copyProperties(domain, domainResponse);
        return new ResponseEntity<>(domainResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<DomainResponse>> getDomains() {
        List<DomainResponse> domainResponses = new ArrayList<>();
        List<Domain> domains = domainService.findAll();
        domains.forEach(domain -> {
            DomainResponse domainResponse = new DomainResponse();
            BeanUtils.copyProperties(domain, domainResponse);
            domainResponses.add(domainResponse);
        });
        return new ResponseEntity<>(domainResponses, HttpStatus.CREATED);
    }
}
