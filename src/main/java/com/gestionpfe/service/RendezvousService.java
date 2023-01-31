package com.gestionpfe.service;

import com.gestionpfe.enums.RendezvousState;
import com.gestionpfe.enums.StudentGroupState;
import com.gestionpfe.exceptions.RendezvousException;
import com.gestionpfe.exceptions.StudentGroupException;
import com.gestionpfe.model.Rendezvous;
import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.rendezvous.RendezvousStudentDeclineRequest;
import com.gestionpfe.model.requests.rendezvous.RendezvousStudentRequest;
import com.gestionpfe.model.requests.rendezvous.RendezvousSupervisorRequest;
import com.gestionpfe.repository.RendezvousRepository;
import com.gestionpfe.repository.StudentGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RendezvousService {

    private final RendezvousRepository rendezvousRepository;
    private final StudentGroupRepository studentGroupRepository;


    @Autowired
    public RendezvousService(RendezvousRepository rendezvousRepository, StudentGroupRepository studentGroupRepository) {
        this.rendezvousRepository = rendezvousRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    public List<Rendezvous> getAllRendezvousByStudentGroup(Long studentGroupId){
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        //TODO: enable this check
//        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
//            throw new StudentGroupException(String.format("student group id %d not accepted to make this request", studentGroupId));
//        }

        return studentGroup.getRendezvous();
    }

    public Rendezvous save(Long studentGroupId, RendezvousStudentRequest request) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();

        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group id %d not accepted to make this request", studentGroupId));
        }

        Rendezvous rendezvous = new Rendezvous();
        rendezvous.setRequest(request.getRequest());
        rendezvous.setRendezvous(null);
        rendezvous.setDecliningMessage(null);
        rendezvous.setRendezvousState(RendezvousState.PENDING);
        rendezvous.setStudentGroup(studentGroup);

        rendezvousRepository.save(rendezvous);
        return rendezvous;
    }

    public Rendezvous updateRendezvousWithDate(Long rendezvousId, Long studentGroupId, RendezvousSupervisorRequest request) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        Optional<Rendezvous> rendezvousOptional = rendezvousRepository.findById(rendezvousId);
        if (rendezvousOptional.isEmpty()) {
            throw new RendezvousException(String.format("rendezvous id %d not found", rendezvousId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();

        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group id %d not accepted to make this request", studentGroupId));
        }

        Rendezvous rendezvous = rendezvousOptional.get();
        rendezvous.setRendezvous(request.getRendezvous());
        rendezvous.setRendezvousState(RendezvousState.WAITING_FOR_VALIDATION);

        rendezvousRepository.save(rendezvous);
        return rendezvous;
    }

    public Rendezvous acceptRendezvous(Long rendezvousId, Long studentGroupId) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        Optional<Rendezvous> rendezvousOptional = rendezvousRepository.findById(rendezvousId);
        if (rendezvousOptional.isEmpty()) {
            throw new RendezvousException(String.format("rendezvous id %d not found", rendezvousId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group id %d not accepted to make this request", studentGroupId));
        }

        Rendezvous rendezvous = rendezvousOptional.get();
        if(rendezvous.getRendezvousState().equals(RendezvousState.PENDING)) {
            throw new RendezvousException(String.format("rendezvous id %d can't be accepted while still in validation", rendezvousId));
        }

        if(rendezvous.getRendezvousState().equals(RendezvousState.REJECTED)) {
            throw new RendezvousException(String.format("rendezvous id %d already rejected", rendezvousId));
        }

        rendezvous.setRendezvousState(RendezvousState.ACCEPTED);

        rendezvousRepository.save(rendezvous);
        return rendezvous;
    }

    public Rendezvous rejectRendezvous(Long rendezvousId, Long studentGroupId, RendezvousStudentDeclineRequest request) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        Optional<Rendezvous> rendezvousOptional = rendezvousRepository.findById(rendezvousId);
        if (rendezvousOptional.isEmpty()) {
            throw new RendezvousException(String.format("rendezvous id %d not found", rendezvousId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group id %d not accepted to make this request", studentGroupId));
        }

        Rendezvous rendezvous = rendezvousOptional.get();
        if(rendezvous.getRendezvousState().equals(RendezvousState.PENDING)) {
            throw new RendezvousException(String.format("rendezvous id %d can't be declined while still in validation", rendezvousId));
        }

        if(rendezvous.getRendezvousState().equals(RendezvousState.ACCEPTED)) {
            throw new RendezvousException(String.format("rendezvous id %d already accepted", rendezvousId));
        }

        rendezvous.setDecliningMessage(request.getDecliningMessage());
        rendezvous.setRendezvousState(RendezvousState.REJECTED);

        rendezvousRepository.save(rendezvous);
        return rendezvous;
    }
}
