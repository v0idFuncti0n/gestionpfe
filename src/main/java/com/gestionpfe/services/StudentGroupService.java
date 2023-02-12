package com.gestionpfe.services;

import com.gestionpfe.email.EmailService;
import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.enums.StudentGroupState;
import com.gestionpfe.exceptions.PFESubjectException;
import com.gestionpfe.exceptions.StudentGroupException;
import com.gestionpfe.exceptions.UserException;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.studentgroup.StudentGroupDriveUrlRequest;
import com.gestionpfe.model.requests.studentgroup.StudentGroupRequest;
import com.gestionpfe.repository.AppUserRepository;
import com.gestionpfe.repository.PFESubjectRepository;
import com.gestionpfe.repository.StudentGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final EmailService emailService;

    @Autowired
    public StudentGroupService(PFESubjectRepository pfeSubjectRepository, StudentGroupRepository studentGroupRepository, AppUserRepository appUserRepository, EmailService emailService) {
        this.pfeSubjectRepository = pfeSubjectRepository;
        this.studentGroupRepository = studentGroupRepository;
        this.appUserRepository = appUserRepository;
        this.emailService = emailService;
    }

    public StudentGroup save(Long pfeSubjectId, StudentGroupRequest studentGroupRequest) {
        Optional<AppUser> studentOptional = appUserRepository.findById(studentGroupRequest.getCurrentStudentId());
        if (studentOptional.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }

        AppUser student = studentOptional.get();

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFESubjectException(String.format("pfe subject id %d not found", pfeSubjectId));
        }

        student.getStudentGroup().forEach(studentGroup -> {
            if (studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
                throw new StudentGroupException(String.format("student id %d already in a accepted group", student.getId()));
            }
        });

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
                if (!sg.getStudentGroupState().equals(StudentGroupState.REJECTED)) {
                    sg.getStudents().forEach(s -> {
                        if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                            groupsAssignedToStudent.incrementAndGet();
                        }
                        if (groupsAssignedToStudent.get() > MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR) {
                            throw new StudentGroupException(String.format("this student %d can't be assigned to more than 3 groups", studentGroupRequest.getCurrentStudentId()));
                        }
                    });
                }
            });
        });

        ArrayList<AppUser> students = new ArrayList<>();
        students.add(student);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setStudents(students);
        studentGroup.setPfeSubject(pfeSubject);
        studentGroup.setStudentGroupState(StudentGroupState.PENDING);
        studentGroup.setDriveUrl(null);
        studentGroup.setDriveUrlPublished(false);

        return studentGroupRepository.save(studentGroup);
    }

    public StudentGroup joinGroup(Long studentGroupId, Long pfeSubjectId, StudentGroupRequest studentGroupRequest) {
        Optional<AppUser> studentOptional = appUserRepository.findById(studentGroupRequest.getCurrentStudentId());
        if (studentOptional.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }
        AppUser student = studentOptional.get();

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFESubjectException(String.format("pfe subject id %d not found", pfeSubjectId));
        }

        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        student.getStudentGroup().forEach(studentGroup -> {
            if (studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
                throw new StudentGroupException(String.format("student id %d already in a accepted group", student.getId()));
            }
        });

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
                if (!sg.getStudentGroupState().equals(StudentGroupState.REJECTED)) {
                    sg.getStudents().forEach(s -> {
                        if (s.getId().equals(studentGroupRequest.getCurrentStudentId())) {
                            groupsAssignedToStudent.incrementAndGet();
                        }
                        if (groupsAssignedToStudent.get() > MAX_PFE_SUBJECT_APPLY_PER_SUPERVISOR) {
                            throw new StudentGroupException(String.format("this student %d can't be assigned to more than 3 groups", studentGroupRequest.getCurrentStudentId()));
                        }
                    });
                }
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

    public StudentGroup removeStudentFromStudentGroup(Long studentGroupId, StudentGroupRequest studentGroupRequest) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        if (studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student %d cannot be removed from and accepted group", studentGroupRequest.getCurrentStudentId()));
        }

        Optional<AppUser> studentOptional = appUserRepository.findById(studentGroupRequest.getCurrentStudentId());
        if (studentOptional.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentGroupRequest.getCurrentStudentId()));
        }

        AppUser student = studentOptional.get();

        studentGroup.getStudents().remove(student);
        studentGroupRepository.save(studentGroup);

        if (studentGroup.getStudents().isEmpty()) {
            studentGroupRepository.deleteById(studentGroup.getId());
        }

        return studentGroup;
    }

    public List<StudentGroup> getCompletedStudentGroupsByPFESubject(Long pfeSubjectId) {

        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if (pfeSubjectOptional.isEmpty()) {
            throw new PFESubjectException(String.format("pfe subject id %d not found!", pfeSubjectId));
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
            throw new PFESubjectException(String.format("pfe subject id %d not found", pfeSubjectId));
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
                if (!Objects.equals(sg.getId(), studentGroup.getId())) {
                    sg.getStudents().remove(student);
                    studentGroupRepository.save(sg);
                }
                if (sg.getStudents().isEmpty()) {
                    studentGroupRepository.deleteById(sg.getId());
                }
            }
        }

        studentGroup.getStudents().forEach(student -> {
            emailService.sendEmail(student.getEmail(), EmailService.buildGroupHasBeenAcceptedEmail(
                            student.getFirstName(),
                            student.getLastName(),
                            studentGroup.getPfeSubject().getSubject(),
                            studentGroup.getPfeSubject().getSupervisor().getFirstName(),
                            studentGroup.getPfeSubject().getSupervisor().getLastName(),
                            studentGroup),
                    EmailService.YOUR_GROUP_HAS_BEEN_ACCEPTED
            );
            student.setAppUserRole(AppUserRole.STUDENT_ACCEPTED_IN_GROUP);
            appUserRepository.save(student);
        });

        studentGroup.setStudentGroupState(StudentGroupState.ACCEPTED);
        studentGroupRepository.save(studentGroup);

        return studentGroup;
    }

    public StudentGroup refuseGroup(Long studentGroupId) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();

        studentGroup.getStudents().forEach(student -> {
            emailService.sendEmail(student.getEmail(), EmailService.buildGroupHasBeenRejectedEmail(
                            student.getFirstName(),
                            student.getLastName(),
                            studentGroup.getPfeSubject().getSubject(),
                            studentGroup.getPfeSubject().getSupervisor().getFirstName(),
                            studentGroup.getPfeSubject().getSupervisor().getLastName(),
                            studentGroup),
                    EmailService.YOUR_GROUP_HAS_BEEN_REJECTED
            );
        });

        studentGroup.setStudentGroupState(StudentGroupState.REJECTED);
        studentGroupRepository.save(studentGroup);
        return studentGroup;
    }

    public StudentGroup addDriveUrl(Long studentGroupId, StudentGroupDriveUrlRequest request) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group %d is not accepted yet", studentGroupId));
        }

        studentGroup.getStudents().forEach(student -> {
            emailService.sendEmail(student.getEmail(), EmailService.buildDriveUrlHasBeenAddedEmail(studentGroup,
                            studentGroup.getPfeSubject().getSubject(),
                            studentGroup.getPfeSubject().getSupervisor().getFirstName(),
                            studentGroup.getPfeSubject().getSupervisor().getLastName(),
                            request.getDriveUrl()
                    ),
                    EmailService.DRIVE_URL_HAS_BEEN_ADDED
            );
        });

        studentGroup.setDriveUrl(request.getDriveUrl());
        studentGroupRepository.save(studentGroup);
        return studentGroup;
    }

    public StudentGroup publishDriveUrl(Long studentGroupId) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if (studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d not found", studentGroupId));
        }

        StudentGroup studentGroup = studentGroupOptional.get();
        if (!studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
            throw new StudentGroupException(String.format("student group %d is not accepted yet", studentGroupId));
        }

        studentGroup.getStudents().forEach(student -> {
            emailService.sendEmail(student.getEmail(), EmailService.buildDriveUrlHasBeenPublishedEmail(student.getFirstName(),
                            student.getLastName(),
                            studentGroup.getPfeSubject().getSubject(),
                            studentGroup.getPfeSubject().getSupervisor().getFirstName(),
                            studentGroup.getPfeSubject().getSupervisor().getLastName(),
                            studentGroup.getDriveUrl()
                    ),
                    EmailService.DRIVE_URL_HAS_BEEN_PUBLISHED
            );
        });

        studentGroup.setDriveUrlPublished(true);
        studentGroupRepository.save(studentGroup);
        return studentGroup;
    }

    public StudentGroup findById(Long studentGroupId) {
        Optional<StudentGroup> studentGroupOptional = studentGroupRepository.findById(studentGroupId);
        if(studentGroupOptional.isEmpty()) {
            throw new StudentGroupException(String.format("student group id %d is not found", studentGroupId));
        }
        return studentGroupOptional.get();
    }

    public StudentGroup findByAcceptedStudent(Long studentId) {
        Optional<AppUser> studentOptional = appUserRepository.findById(studentId);
        if (studentOptional.isEmpty()) {
            throw new UserException(String.format("student id %d not found!", studentId));
        }

        AppUser student = studentOptional.get();
        final StudentGroup[] acceptedStudentGroup = new StudentGroup[1];
        student.getStudentGroup().forEach(studentGroup -> {
            if (studentGroup.getStudentGroupState().equals(StudentGroupState.ACCEPTED)) {
                acceptedStudentGroup[0] = studentGroup;
            } else {
                throw new StudentGroupException(String.format("student id %d not accepted in any group", studentId));
            }
        });
        return acceptedStudentGroup[0];
    }
}
