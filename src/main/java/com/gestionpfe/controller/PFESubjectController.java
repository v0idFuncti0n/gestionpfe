package com.gestionpfe.controller;

import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.requests.PFESubjectRequest;
import com.gestionpfe.model.responses.PFESubjectResponse;
import com.gestionpfe.services.PFESubjectService;
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
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectBySupervisor(@PathVariable Long supervisorId){
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

    @GetMapping(path = "/search")
    public ResponseEntity<List<PFESubjectResponse>> findByKeyword(@RequestParam String keyword) {
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByKeyword(keyword).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/pfe-subject/{pfe-subject-id}")
    public ResponseEntity<PFESubjectResponse> findById(@PathVariable(name = "pfe-subject-id") Long pfeSubjectId) {
        PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
        PFESubject pfeSubject = pfeSubjectService.findById(pfeSubjectId);
        BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
        return new ResponseEntity<>(pfeSubjectResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/university/{university-id}")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByUniversity(@PathVariable(name = "university-id") Long universityId){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByUniversity(universityId).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/university/{university-id}/search")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByUniversityAndKeyword(@PathVariable(name = "university-id") Long universityId, @RequestParam String keyword){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByUniversityAndKeyword(universityId, keyword).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/establishment/{establishment-id}")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByEstablishment(@PathVariable(name = "establishment-id") Long establishmentId){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByEstablishment(establishmentId).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/establishment/{establishment-id}/search")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByEstablishmentAndKeyword(@PathVariable(name = "establishment-id") Long establishmentId, @RequestParam String keyword){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByEstablishmentAndKeyword(establishmentId, keyword).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/department/{department-id}")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByDepartment(@PathVariable(name = "department-id") Long departmentId){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByDepartment(departmentId).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/department/{department-id}/search")
    public ResponseEntity<List<PFESubjectResponse>> getAllPFESubjectsByDepartmentAndKeyword(@PathVariable(name = "department-id") Long departmentId, @RequestParam String keyword){
        List<PFESubjectResponse> pfeSubjectResponses = new ArrayList<>();
        pfeSubjectService.findByDepartmentAndKeyword(departmentId, keyword).forEach(pfeSubject -> {
            PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
            BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
            pfeSubjectResponses.add(pfeSubjectResponse);
        });

        return new ResponseEntity<>(pfeSubjectResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/student-group/{student-group-id}")
    public ResponseEntity<PFESubjectResponse> findByStudentGroup(@PathVariable(name = "student-group-id") Long studentGroupId) {
        PFESubjectResponse pfeSubjectResponse = new PFESubjectResponse();
        PFESubject pfeSubject = pfeSubjectService.findByStudentGroup(studentGroupId);
        BeanUtils.copyProperties(pfeSubject, pfeSubjectResponse);
        return new ResponseEntity<>(pfeSubjectResponse, HttpStatus.OK);
    }

}
