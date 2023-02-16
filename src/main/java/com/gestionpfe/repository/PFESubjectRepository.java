package com.gestionpfe.repository;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PFESubjectRepository extends CrudRepository<PFESubject, Long> {

    PFESubject findBySubject(String subject);

    List<PFESubject> findBySupervisor(AppUser supervisor);

    List<PFESubject> findPFESubjectBySubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContainsOrderByIdAsc(String keyword1, String keyword2, String keyword3);
    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_University_IdOrderByIdAsc(Long universityId);

    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_IdOrderByIdAsc(Long establishmentId);
    List<PFESubject> findPFESubjectBySupervisor_Department_IdOrderByIdAsc(Long departmentId);
}
