package com.gestionpfe.controller;

import com.gestionpfe.model.Establishment;
import com.gestionpfe.model.responses.EstablishmentResponse;
import com.gestionpfe.services.EstablishmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/establishment")
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    public EstablishmentController(EstablishmentService establishmentService) {
        this.establishmentService = establishmentService;
    }

    @GetMapping
    public ResponseEntity<List<EstablishmentResponse>> getAllEstablishments() {
        List<EstablishmentResponse> establishmentRespons = new ArrayList<>();
        establishmentService.findAll().forEach(establishment -> {
            EstablishmentResponse establishmentResponse = new EstablishmentResponse();
            BeanUtils.copyProperties(establishment, establishmentResponse);
            establishmentRespons.add(establishmentResponse);
        });

        return new ResponseEntity<>(establishmentRespons, HttpStatus.OK);
    }

    @GetMapping(path = "/university/{university-id}")
    public ResponseEntity<List<EstablishmentResponse>> findEstablishmentsByUniversity(@PathVariable("university-id") Long universityId) {
        List<EstablishmentResponse> establishmentResponses = new ArrayList<>();
        List<Establishment> establishments = establishmentService.findEstablishmentByUniversity(universityId);
        establishments.forEach(establishment -> {
            EstablishmentResponse establishmentResponse = new EstablishmentResponse();
            BeanUtils.copyProperties(establishment, establishmentResponse);
            establishmentResponses.add(establishmentResponse);
        });

        return new ResponseEntity<>(establishmentResponses, HttpStatus.OK);
    }
}
