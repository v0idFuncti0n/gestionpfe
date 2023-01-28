package com.gestionpfe.service;

import com.gestionpfe.exceptions.PFEStageException;
import com.gestionpfe.exceptions.StudentGroupException;
import com.gestionpfe.exceptions.UserException;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.StudentGroupRequest;
import com.gestionpfe.repository.AppUserRepository;
import com.gestionpfe.repository.PFESubjectRepository;
import com.gestionpfe.repository.StudentGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class StudentGroupService {

    private static final int MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR = 3;

    private final PFESubjectRepository pfeSubjectRepository;

    private final StudentGroupRepository studentGroupRepository;

    private final AppUserRepository appUserRepository;

    public StudentGroupService(PFESubjectRepository pfeSubjectRepository, StudentGroupRepository studentGroupRepository, AppUserRepository appUserRepository) {
        this.pfeSubjectRepository = pfeSubjectRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.appUserRepository = appUserRepository;
    }

    public StudentGroup save(StudentGroupRequest studentGroupRequest) {
        Optional<AppUser> student = appUserRepository.findById(studentGroupRequest.getCurrentStudentId());
        if(student.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(studentGroupRequest.getPfeSubjectId());
        if(pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found", studentGroupRequest.getPfeSubjectId()));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();
        pfeSubject.getStudentGroups().forEach(studentGroup -> {
            studentGroup.getStudents().forEach(s -> {
                if(s.getId().equals(studentGroupRequest.getCurrentStudentId())){
                    throw new StudentGroupException(String.format("this student's id %d already in a group in this pfe subject!", s.getId()));
                }
            });
        });

        AppUser supervisor = pfeSubject.getSupervisor();
        AtomicInteger groupsAssignedToStudent = new AtomicInteger(1);
        supervisor.getPfeSubjects().forEach(ps -> {
            ps.getStudentGroups().forEach(sg -> {
                sg.getStudents().forEach(s -> {
                    if(s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                        groupsAssignedToStudent.incrementAndGet();
                    }
                    if(groupsAssignedToStudent.get() > MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR) {
                        throw new StudentGroupException(String.format("this student %d can't be assigned to more than 3 groups", studentGroupRequest.getCurrentStudentId()));
                    }
                });
            });
        });
        log.error("{}", groupsAssignedToStudent.get());

        ArrayList<AppUser> students = new ArrayList<>();
        students.add(student.get());

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setStudents(students);
        studentGroup.setPfeSubject(pfeSubject);


        return studentGroupRepository.save(studentGroup);
    }

    public StudentGroup joinGroup(Long studentGroupId, StudentGroupRequest studentGroupRequest) {
        return null;
    }

    public List<StudentGroup> findByPFESubject(Long pfeSubjectId) {
        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if(pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found", pfeSubjectId));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();

        return pfeSubject.getStudentGroups();
    }
}
