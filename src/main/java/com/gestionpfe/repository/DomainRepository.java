package com.gestionpfe.repository;

import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.model.Domain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DomainRepository extends CrudRepository<Domain, Long> {

    Optional<Domain> findByName(String name);
    List<Domain> findByAppUserRole(AppUserRole appUserRole);

}
