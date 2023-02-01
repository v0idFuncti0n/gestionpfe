package com.gestionpfe.controller;

import com.gestionpfe.model.Department;
import com.gestionpfe.model.responses.DepartmentResponse;
import com.gestionpfe.services.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    @Autowired
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping(path = "/university/{university-id}")
    public ResponseEntity<List<DepartmentResponse>> findDepartmentsByUniversity(@PathVariable("university-id") Long universityId) {
        List<DepartmentResponse> departmentsResponses = new ArrayList<>();
        departmentService.findByUniversity(universityId).forEach(department -> {
            DepartmentResponse departmentResponse = new DepartmentResponse();
            BeanUtils.copyProperties(department, departmentResponse);
            departmentsResponses.add(departmentResponse);
        });

        return new ResponseEntity<>(departmentsResponses, HttpStatus.OK);
    }
}
