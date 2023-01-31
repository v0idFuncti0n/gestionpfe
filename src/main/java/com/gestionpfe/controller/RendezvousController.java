package com.gestionpfe.controller;

import com.gestionpfe.model.Rendezvous;
import com.gestionpfe.model.requests.rendezvous.RendezvousStudentDeclineRequest;
import com.gestionpfe.model.requests.rendezvous.RendezvousStudentRequest;
import com.gestionpfe.model.requests.rendezvous.RendezvousSupervisorRequest;
import com.gestionpfe.model.responses.RendezvousResponse;
import com.gestionpfe.service.RendezvousService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/rendezvous")
public class RendezvousController {

    private final RendezvousService rendezvousService;

    public RendezvousController(RendezvousService rendezvousService) {
        this.rendezvousService = rendezvousService;
    }


    @GetMapping(path = "/{student-group-id}")
    public ResponseEntity<List<RendezvousResponse>> getAllRendezvousByStudentGroup(@PathVariable(name = "student-group-id") Long studentGroupId) {
        List<RendezvousResponse> rendezvousResponsesList = new ArrayList<>();
        List<Rendezvous> rendezvousList = rendezvousService.getAllRendezvousByStudentGroup(studentGroupId);

        rendezvousList.forEach(rendezvous -> {
            RendezvousResponse rendezvousResponse = new RendezvousResponse();
            BeanUtils.copyProperties(rendezvous, rendezvousResponse);
            rendezvousResponsesList.add(rendezvousResponse);
        });

        return new ResponseEntity<>(rendezvousResponsesList, HttpStatus.OK);
    }

    @PostMapping(path = "/{student-group-id}")
    public ResponseEntity<RendezvousResponse> createRendezvous(@PathVariable(name = "student-group-id") Long studentGroupId, @RequestBody RendezvousStudentRequest request) {
        RendezvousResponse rendezvousResponse = new RendezvousResponse();
        Rendezvous rendezvous = rendezvousService.save(studentGroupId, request);
        BeanUtils.copyProperties(rendezvous, rendezvousResponse);
        return new ResponseEntity<>(rendezvousResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/{rendezvous-id}/group/{student-group-id}")
    public ResponseEntity<RendezvousResponse> updateRendezvousWithDate(@PathVariable(name = "rendezvous-id") Long rendezvousId, @PathVariable(name = "student-group-id") Long studentGroupId,  @RequestBody RendezvousSupervisorRequest request) {
        RendezvousResponse rendezvousResponse = new RendezvousResponse();
        Rendezvous rendezvous = rendezvousService.updateRendezvousWithDate(rendezvousId, studentGroupId, request);
        BeanUtils.copyProperties(rendezvous, rendezvousResponse);
        return new ResponseEntity<>(rendezvousResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/{rendezvous-id}/group/{student-group-id}/accept")
    public ResponseEntity<RendezvousResponse> acceptRendezvous(@PathVariable(name = "rendezvous-id") Long rendezvousId, @PathVariable(name = "student-group-id") Long studentGroupId) {
        RendezvousResponse rendezvousResponse = new RendezvousResponse();
        Rendezvous rendezvous = rendezvousService.acceptRendezvous(rendezvousId, studentGroupId);
        BeanUtils.copyProperties(rendezvous, rendezvousResponse);
        return new ResponseEntity<>(rendezvousResponse, HttpStatus.OK);
    }

    @PostMapping(path = "/{rendezvous-id}/group/{student-group-id}/reject")
    public ResponseEntity<RendezvousResponse> rejectRendezvous(@PathVariable(name = "rendezvous-id") Long rendezvousId, @PathVariable(name = "student-group-id") Long studentGroupId, @RequestBody RendezvousStudentDeclineRequest request) {
        RendezvousResponse rendezvousResponse = new RendezvousResponse();
        Rendezvous rendezvous = rendezvousService.rejectRendezvous(rendezvousId, studentGroupId, request);
        BeanUtils.copyProperties(rendezvous, rendezvousResponse);
        return new ResponseEntity<>(rendezvousResponse, HttpStatus.OK);
    }
}
