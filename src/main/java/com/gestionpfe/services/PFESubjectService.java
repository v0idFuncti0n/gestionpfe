package com.gestionpfe.services;

import com.gestionpfe.exceptions.PFESubjectException;
import com.gestionpfe.exceptions.StudentGroupException;
import com.gestionpfe.exceptions.UserException;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.StudentGroup;
import com.gestionpfe.model.requests.PFESubjectRequest;
import com.gestionpfe.repository.PFESubjectRepository;
import com.gestionpfe.repository.UniversityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PFESubjectService {

    private final PFESubjectRepository pfeSubjectRepository;
    private final AppUserService appUserService;
    private final StudentGroupService studentGroupService;

    private final UniversityRepository universityRepository;

    @Autowired
    public PFESubjectService(PFESubjectRepository pfeSubjectRepository, AppUserService appUserService, StudentGroupService studentGroupService, UniversityRepository universityRepository) {
        this.pfeSubjectRepository = pfeSubjectRepository;
        this.appUserService = appUserService;
        this.studentGroupService = studentGroupService;
        this.universityRepository = universityRepository;
    }

    public PFESubject save(PFESubjectRequest pfeSubjectRequest) {
        PFESubject pfeSubject = new PFESubject();

        AppUser supervisor = appUserService.findById(pfeSubjectRequest.getSupervisor());
        if(supervisor == null) {
            throw new PFESubjectException(String.format("Supervisor id %d not found!", pfeSubjectRequest.getSupervisor()));
        }

        if(pfeSubjectRepository.findBySubject(pfeSubjectRequest.getSubject()) != null) {
            throw new PFESubjectException(String.format("Subject already exists! : %s", pfeSubjectRequest.getSubject()));
        }

        pfeSubject.setSubject(pfeSubjectRequest.getSubject());
        pfeSubject.setDescription(pfeSubjectRequest.getDescription());
        pfeSubject.setSupervisor(supervisor);
        pfeSubject.setPublished(false);
        pfeSubject.setStudentGroups(new ArrayList<>());
        pfeSubject.setGroupNumber(pfeSubjectRequest.getGroupNumber());

        return pfeSubjectRepository.save(pfeSubject);
    }

    public Iterable<PFESubject> findAll() {
        return pfeSubjectRepository.findAll();
    }

    public Iterable<PFESubject> findBySupervisor(Long supervisorId) {
        AppUser supervisor = appUserService.findById(supervisorId);
        if(supervisor == null) {
            throw new UserException(String.format("supervisor id %d not found", supervisorId));
        }
        return pfeSubjectRepository.findBySupervisor(supervisor);
    }

    public PFESubject findById(Long pfeSubjectId) {
        Optional<PFESubject> pfeSubjectOptional = pfeSubjectRepository.findById(pfeSubjectId);
        if(pfeSubjectOptional.isEmpty()) {
            throw new PFESubjectException(String.format("pfe subject id %d not found", pfeSubjectId));
        }
        return pfeSubjectOptional.get();
    }

    public List<PFESubject> findByKeyword(String keyword) {
        return pfeSubjectRepository.findPFESubjectBySubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContainsOrderById(keyword, keyword, keyword);
    }

    public List<PFESubject> findByUniversity(Long universityId) {
        return pfeSubjectRepository.findPFESubjectBySupervisor_Department_Establishment_University_IdOrderById(universityId);
    }

    public List<PFESubject> findByUniversityAndKeyword(Long universityId, String keyword) {
        List<PFESubject> pfeSubjectsByUniversity = this.findByUniversity(universityId);
        List<PFESubject> pfeSubjectsByKeyword = this.findByKeyword(keyword);

        return pfeSubjectsByKeyword.stream()
                .distinct()
                .filter(pfeSubjectsByUniversity::contains)
                .collect(Collectors.toList());
    }

    public List<PFESubject> findByEstablishment(Long establishmentId) {
        return pfeSubjectRepository.findPFESubjectBySupervisor_Department_Establishment_IdOrderById(establishmentId);
    }

    public List<PFESubject> findByEstablishmentAndKeyword(Long establishmentId, String keyword) {
        List<PFESubject> pfeSubjectsByEstablishment = this.findByEstablishment(establishmentId);
        List<PFESubject> pfeSubjectsByKeyword = this.findByKeyword(keyword);

        return pfeSubjectsByKeyword.stream()
                .distinct()
                .filter(pfeSubjectsByEstablishment::contains)
                .collect(Collectors.toList());
    }

    public List<PFESubject> findByDepartment(Long departmentId) {
        return pfeSubjectRepository.findPFESubjectBySupervisor_Department_IdOrderById(departmentId);
    }

    public List<PFESubject> findByDepartmentAndKeyword(Long departmentId, String keyword) {
        List<PFESubject> pfeSubjectsByDepartment = this.findByDepartment(departmentId);
        List<PFESubject> pfeSubjectsByKeyword = this.findByKeyword(keyword);

        return pfeSubjectsByKeyword.stream()
                .distinct()
                .filter(pfeSubjectsByDepartment::contains)
                .collect(Collectors.toList());
    }

    public PFESubject findByStudentGroup(Long studentGroupId) {
        StudentGroup studentGroup = this.studentGroupService.findById(studentGroupId);
        return studentGroup.getPfeSubject();
    }
}
