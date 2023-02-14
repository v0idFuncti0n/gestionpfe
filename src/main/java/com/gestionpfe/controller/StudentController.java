package com.gestionpfe.controller;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.responses.StudentResponse;
import com.gestionpfe.model.responses.SupervisorResponse;
import com.gestionpfe.services.AppUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

    private final AppUserService appUserService;
    public StudentController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(path = "/{student-id}")
    public ResponseEntity<StudentResponse> findStudentById(@PathVariable("student-id") Long studentId) {
        StudentResponse studentResponse = new StudentResponse();
        AppUser student = appUserService.findById(studentId);
        BeanUtils.copyProperties(student, studentResponse);

        return new ResponseEntity<>(studentResponse, HttpStatus.OK);
    }
}
