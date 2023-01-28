package com.gestionpfe.controller;

import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.requests.PFESubjectRequest;
import com.gestionpfe.model.responses.PFESubjectResponse;
import com.gestionpfe.service.PFESubjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/pfe-subject")
public class PFESubjectController {

    private final PFESubjectService pfeSubjectService;

    @Autowired
    public PFESubjectController(PFESubjectService pfeSubjectService) {
        this.pfeSubjectService = pfeSubjectService;
    }

    @GetMapping
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubject(){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findAll().forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);

            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/{supervisorId}")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubject(@PathVariable Long supervisorId){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findBySupervisor(supervisorId).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PFESubjectResponse> createPFESubject(@RequestBody PFESubjectRequest request) {
        PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
        PFESubject pfeSubject = pfeSubjectService.save(request);
        BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
        return new ResponseEntity<>(pfeSubjectResponse, HttpStatus.CREATED);
    }

}
