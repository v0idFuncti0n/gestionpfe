package com.gestionpfe.controller;

import com.gestionpfe.model.responses.UniversityResponse;
import com.gestionpfe.services.UniversityService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/university")
public class UniversityController {

    private final UniversityService universityService;

    public UniversityController(UniversityService universityService) {
        this.universityService = universityService;
    }

    @GetMapping
    public ResponseEntity<List<UniversityResponse>> getAllUniversities() {
        List<UniversityResponse> universityResponses = new ArrayList<>();
        universityService.findAll().forEach(university -> {
            UniversityResponse universityResponse = new UniversityResponse();
            BeanUtils.copyProperties(university, universityResponse);
            universityResponses.add(universityResponse);
        });

        return new ResponseEntity<>(universityResponses, HttpStatus.OK);
    }
}
