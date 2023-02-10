package com.gestionpfe.repository;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PFESubjectRepository extends CrudRepository<PFESubject, Long> {

    PFESubject findBySubject(String subject);

    List<PFESubject> findBySupervisor(AppUser supervisor);

    List<PFESubject> findPFESubjectBySubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContains(String keyword1, String keyword2, String keyword3);
    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_University_Id(Long universityId);
    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_University_IdAndSubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContains(Long universityId, String keyword1, String keyword2, String keyword3);
    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_Id(Long establishmentId);
    List<PFESubject> findPFESubjectBySupervisor_Department_Establishment_IdAndSubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContains(Long establishmentId, String keyword1, String keyword2, String keyword3);
    List<PFESubject> findPFESubjectBySupervisor_Department_Id(Long departmentId);
    List<PFESubject> findPFESubjectBySupervisor_Department_IdAndSubjectContainsOrSupervisor_FirstNameContainsOrSupervisor_LastNameContains(Long departmentId, String keyword1, String keyword2, String keyword3);
}
