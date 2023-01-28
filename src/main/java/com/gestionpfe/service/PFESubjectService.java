package com.gestionpfe.service;

import com.gestionpfe.exceptions.PFEStageException;
import com.gestionpfe.exceptions.UserException;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import com.gestionpfe.model.requests.PFESubjectRequest;
import com.gestionpfe.repository.PFESubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class PFESubjectService {

    private final PFESubjectRepository pfeSubjectRepository;
    private final AppUserService appUserService;

    @Autowired
    public PFESubjectService(PFESubjectRepository pfeSubjectRepository, AppUserService appUserService) {
        this.pfeSubjectRepository = pfeSubjectRepository;
        this.appUserService = appUserService;
    }

    public PFESubject save(PFESubjectRequest pfeSubjectRequest) {
        PFESubject pfeSubject = new PFESubject();

        AppUser supervisor = appUserService.findById(pfeSubjectRequest.getSupervisor());
        if(supervisor == null) {
            throw new PFEStageException(String.format("Supervisor id %d not found!", pfeSubjectRequest.getSupervisor()));
        }

        if(pfeSubjectRepository.findBySubject(pfeSubjectRequest.getSubject()) != null) {
            throw new PFEStageException(String.format("Subject already exists! : %s", pfeSubjectRequest.getSubject()));
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
            throw new UserException(String.format("supervisor id %d not found ", supervisorId));
        }
        return pfeSubjectRepository.findBySupervisor(supervisor);
    }
}
