package com.gestionpfe.controller;

import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.studentgroup.StudentGroupDriveUrlRequest;
import com.gestionpfe.model.requests.studentgroup.StudentGroupRequest;
import com.gestionpfe.model.responses.StudentGroupResponse;
import com.gestionpfe.services.StudentGroupService;
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


    @PostMapping(path = "/pfe-subject/{pfe-subject-id}")
    public ResponseEntity<StudentGroupResponse> createStudentGroup(@PathVariable(name = "pfe-subject-id") Long pfeSubjectId, @RequestBody StudentGroupRequest studentGroupRequest) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.save(pfeSubjectId, studentGroupRequest);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);

        return new ResponseEntity<>(studentGroupResponse, HttpStatus.CREATED);
    }

    @PostMapping(path = "/pfe-subject/{pfe-subject-id}/join/{group-id}")
    public ResponseEntity<StudentGroupResponse> joinGroup(@PathVariable(name = "group-id") Long studentGroupId, @PathVariable(name = "pfe-subject-id") Long pfeSubjectId, @RequestBody StudentGroupRequest request) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.joinGroup(studentGroupId, pfeSubjectId, request);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/remove-student/{group-id}")
    public ResponseEntity<StudentGroupResponse> removeStudentFromGroup(@PathVariable(name = "group-id") Long studentGroupId, @RequestBody StudentGroupRequest request) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.removeStudentFromStudentGroup(studentGroupId, request);
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

    @PostMapping(path = "/refuse/{group-id}")
    public ResponseEntity<StudentGroupResponse> refuseGroup(@PathVariable(name = "group-id") Long studentGroupId) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.refuseGroup(studentGroupId);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @PostMapping(path = "{group-id}/add-drive/")
    public ResponseEntity<StudentGroupResponse> addDriveUrl(@PathVariable(name = "group-id") Long studentGroupId, @RequestBody StudentGroupDriveUrlRequest request) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.addDriveUrl(studentGroupId, request);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @PostMapping(path = "{group-id}/publish-drive-link/")
    public ResponseEntity<StudentGroupResponse> publishDriveUrl(@PathVariable(name = "group-id") Long studentGroupId) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.publishDriveUrl(studentGroupId);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/student/{student-id}")
    public ResponseEntity<StudentGroupResponse> findByAcceptedStudentId(@PathVariable(name = "student-id") Long studentId) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.findByAcceptedStudent(studentId);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

    @GetMapping(path = "/{student-group-id}")
    public ResponseEntity<StudentGroupResponse> findById(@PathVariable(name = "student-group-id") Long studentGroupId) {
        StudentGroupResponse studentGroupResponse = new StudentGroupResponse();
        StudentGroup studentGroup = studentGroupService.findById(studentGroupId);
        BeanUtils.copyProperties(studentGroup, studentGroupResponse);
        return new ResponseEntity<>(studentGroupResponse, HttpStatus.OK);
    }

}
