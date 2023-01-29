package com.gestionpfe.controller;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.StudentGroupRequest;
import com.gestionpfe.model.responses.StudentGroupResponse;
import com.gestionpfe.service.StudentGroupService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/student-group")
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    public StudentGroupController(StudentGroupService studentGroupService) {
        this.studentGroupService = studentGroupService;
    }

    @GetMapping(path = "/{pfe-subject-id}")
    public ResponseEntity<List<StudentGroupResponse>> getStudentGroupsByPFESubject(@PathVariable("pfe-subject-id") Long pfeSubjectId) {
        List<StudentGroupResponse> studentGroupResponses = new ArrayList<>();
        List<StudentGroup> studentGroups = studentGroupService.findByPFESubject(pfeSubjectId);
        studentGroups.forEach(studentGroup -> {
            StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
            BeanUtils.copyProperties(studentGroup, studentGroupResponse);
            studentGroupResponses.add(studentGroupResponse);
        });

        return new ResponseEntity<>(studentGroupResponses, HttpStatus.OK);
    }

    @GetMapping(path = "/completed-groups/{pfe-subject-id}")
    public ResponseEntity<List<StudentGroupResponse>> getCompletedStudentGroupsByPFESubject(@PathVariable("pfe-subject-id") Long pfeSubjectId) {
        List<StudentGroupResponse> studentGroupResponses = new ArrayList<>();
        List<StudentGroup> studentGroups = studentGroupService.getCompletedStudentGroupsByPFESubject(pfeSubjectId);
        studentGroups.forEach(studentGroup -> {
            StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
            BeanUtils.copyProperties(studentGroup, studentGroupResponse);
            studentGroupResponses.add(studentGroupResponse);
        });

        return new ResponseEntity<>(studentGroupResponses, HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<StudentGroupResponse> createStudentGroup(@RequestBody StudentGroupRequest studentGroupRequest) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.save(studentGroupRequest);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);

        return new ResponseEntity<>(studentGroupResponse, HttpStatus.CREATED);
    }

    @PostMapping(path = "/join/{group-id}")
    public ResponseEntity<StudentGroupResponse> joinGroup(@PathVariable(name = "group-id") Long studentGroupId, @RequestBody StudentGroupRequest request) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.joinGroup(studentGroupId, request);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/accept/{group-id}")
    public ResponseEntity<StudentGroupResponse> acceptGroup(@PathVariable(name = "group-id") Long studentGroupId) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.acceptGroup(studentGroupId);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }
}
