package com.gestionpfe.service;

import com.gestionpfe.enums.StudentGroupState;
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

import java.util.*;
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
        if (student.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(studentGroupRequest.getPfeSubjectId());
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found", studentGroupRequest.getPfeSubjectId()));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();
        pfeSubject.getStudentGroups().forEach(studentGroup -> {
            studentGroup.getStudents().forEach(s -> {
                if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                    throw new StudentGroupException(String.format("this student's id %d already in a group in this pfe subject!", s.getId()));
                }
            });
        });

        AppUser supervisor = pfeSubject.getSupervisor();
        AtomicInteger groupsAssignedToStudent = new AtomicInteger(1);
        supervisor.getPfeSubjects().forEach(ps -> {
            ps.getStudentGroups().forEach(sg -> {
                sg.getStudents().forEach(s -> {
                    if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                        groupsAssignedToStudent.incrementAndGet();
                    }
                    if (groupsAssignedToStudent.get() > MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR) {
                        throw new StudentGroupException(String.format("this student %d can't be assigned to more than 3 groups", studentGroupRequest.getCurrentStudentId()));
                    }
                });
            });
        });

        ArrayList<AppUser> students = new ArrayList<>();
        students.add(student.get());

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setStudents(students);
        studentGroup.setPfeSubject(pfeSubject);
        studentGroup.setStudentGroupState(StudentGroupState.PENDING);

        return studentGroupRepository.save(studentGroup);
    }

    public StudentGroup joinGroup(Long studentGroupId, StudentGroupRequest studentGroupRequest) {
        Optional<AppUser> studentOptional = appUserRepository.findById(studentGroupRequest.getCurrentStudentId());
        if (studentOptional.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }
        AppUser student = studentOptional.get();

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(studentGroupRequest.getPfeSubjectId());
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found", studentGroupRequest.getPfeSubjectId()));
        }

        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();
        pfeSubject.getStudentGroups().forEach(studentGroup -> {
            studentGroup.getStudents().forEach(s -> {
                if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                    throw new StudentGroupException(String.format("this student's id %d already in a group in this pfe subject!", s.getId()));
                }
            });
        });

        AppUser supervisor = pfeSubject.getSupervisor();
        AtomicInteger groupsAssignedToStudent = new AtomicInteger(1);
        supervisor.getPfeSubjects().forEach(ps -> {
            ps.getStudentGroups().forEach(sg -> {
                sg.getStudents().forEach(s -> {
                    if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                        groupsAssignedToStudent.incrementAndGet();
                    }
                    if (groupsAssignedToStudent.get() > MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR) {
                        throw new StudentGroupException(String.format("this student %d can't be assigned to more than 3 groups", studentGroupRequest.getCurrentStudentId()));
                    }
                });
            });
        });

        StudentGroup studentGroup = studentGroupOptional.get();
        if (studentGroup.getStudents().size() >= pfeSubject.getGroupNumber()) {
            throw new StudentGroupException(String.format("this student %d can't be assigned to a full group", studentGroupRequest.getCurrentStudentId()));
        }

        studentGroup.getStudents().add(student);
        studentGroupRepository.save(studentGroup);

        return studentGroup;
    }

    public List<StudentGroup> getCompletedStudentGroupsByPFESubject(Long pfeSubjectId) {

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found!", pfeSubjectId));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();
        List<StudentGroup> completedStudentGroups = new ArrayList<>();

        pfeSubject.getStudentGroups().forEach(studentGroup -> {
            if (pfeSubject.getGroupNumber() == studentGroup.getStudents().size()) {
                completedStudentGroups.add(studentGroup);
            }
        });

        return completedStudentGroups;
    }

    public List<StudentGroup> findByPFESubject(Long pfeSubjectId) {
        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFEStageException(String.format("pfe subject id %d not found", pfeSubjectId));
        }

        PFESubject pfeSubject = pfeSubjectOptional.get();

        return pfeSubject.getStudentGroups();
    }

    public StudentGroup acceptGroup(Long studentGroupId) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();

        for (AppUser student : new ArrayList<>(studentGroup.getStudents())) {
            for (StudentGroup sg : new ArrayList<>(student.getStudentGroup())) {
                if(!Objects.equals(sg.getId(), studentGroup.getId())) {
                    sg.getStudents().remove(student);
                    studentGroupRepository.save(sg);
                }
                log.error("{}", sg.getStudents().isEmpty());
                if(sg.getStudents().isEmpty()) {
                    studentGroupRepository.deleteById(sg.getId());
                }
            }
        }

        studentGroup.setStudentGroupState(StudentGroupState.ACCEPTED);
        studentGroupRepository.save(studentGroup);

        return studentGroup;
    }
}
