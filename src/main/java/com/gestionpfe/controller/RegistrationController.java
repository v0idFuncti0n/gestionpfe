package com.gestionpfe.controller;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.requests.StudentRegistrationRequest;
import com.gestionpfe.model.requests.SupervisorRegistrationRequest;
import com.gestionpfe.model.responses.StudentResponse;
import com.gestionpfe.model.responses.SupervisorResponse;
import com.gestionpfe.services.RegistrationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/v1/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/supervisor")
    public ResponseEntity<?> registerSupervisor(@Valid @RequestBody SupervisorRegistrationRequest request) {
        SupervisorResponse supervisorResponse = new SupervisorResponse();

        AppUser supervisor = registrationService.registerSupervisor(request);;
        BeanUtils.copyProperties(supervisor, supervisorResponse);
        return new ResponseEntity<>(supervisorResponse, HttpStatus.CREATED);
    }

    @PostMapping(path = "/student")
    public ResponseEntity<StudentResponse> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        StudentResponse studentResponse = new StudentResponse();

        AppUser student = registrationService.registerStudent(request);
        BeanUtils.copyProperties(student, studentResponse);
        return new ResponseEntity<>(studentResponse, HttpStatus.CREATED);
    }

    @GetMapping(path = "confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}
