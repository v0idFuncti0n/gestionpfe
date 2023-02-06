package com.gestionpfe.controller;

import com.gestionpfe.model.Branch;
import com.gestionpfe.model.responses.BranchResponse;
import com.gestionpfe.services.BranchService;
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
@RequestMapping(path = "/api/v1/branch")
public class BranchController {
    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @GetMapping(path = "/department/{department-id}")
    public ResponseEntity<List<BranchResponse>> findBranchByDepartment(@PathVariable("department-id") Long departmentId) {
        List<BranchResponse> branchResponses = new ArrayList<>();
        branchService.findByDepartment(departmentId).forEach(branch -> {
            BranchResponse branchResponse = new BranchResponse();
            BeanUtils.copyProperties(branch, branchResponse);
            branchResponses.add(branchResponse);
        });

        return new ResponseEntity<>(branchResponses, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<BranchResponse>> getAllBranches() {
        List<BranchResponse> branchResponses = new ArrayList<>();
        List<Branch> branches = branchService.findAll();
        branches.forEach(branch -> {
            BranchResponse branchResponse = new BranchResponse();
            BeanUtils.copyProperties(branch, branchResponse);
            branchResponses.add(branchResponse);
        });

        return new ResponseEntity<>(branchResponses, HttpStatus.OK);
    }
}
