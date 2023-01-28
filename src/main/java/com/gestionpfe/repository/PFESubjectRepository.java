package com.gestionpfe.repository;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PFESubjectRepository extends CrudRepository<PFESubject, Long> {

    PFESubject findBySubject(String subject);

    List<PFESubject> findBySupervisor(AppUser supervisor);
}
