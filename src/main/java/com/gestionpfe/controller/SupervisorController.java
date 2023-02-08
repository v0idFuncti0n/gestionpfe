package com.gestionpfe.controller;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.Branch;
import com.gestionpfe.model.responses.BranchResponse;
import com.gestionpfe.model.responses.SupervisorResponse;
import com.gestionpfe.services.AppUserService;
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
@RequestMapping(path = "/api/v1/supervisor")
public class SupervisorController {
    private final AppUserService appUserService;

    public SupervisorController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping(path = "/{supervisor-id}")
    public ResponseEntity<SupervisorResponse> findSupervisorById(@PathVariable("supervisor-id") Long supervisorId) {
        SupervisorResponse supervisorResponse = new SupervisorResponse();
        AppUser supervisor = appUserService.findById(supervisorId);
        BeanUtils.copyProperties(supervisor, supervisorResponse);

        return new ResponseEntity<>(supervisorResponse, HttpStatus.OK);
    }
}
