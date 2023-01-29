package com.gestionpfe.security.validators;

import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.model.Domain;
import com.gestionpfe.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class SupervisorEmailValidator implements Predicate<String> {

    private final String SUPERVISOR_EMAIL_PATTERN = "\\w+([-+.']\\w+)*";
    private final DomainRepository domainRepository;

    @Autowired
    public SupervisorEmailValidator(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Override
    public boolean test(String email) {
        List<Domain> supervisorDomains = domainRepository.findByAppUserRole(AppUserRole.SUPERVISOR);
        for(Domain domain : supervisorDomains) {
            if(email.matches(SUPERVISOR_EMAIL_PATTERN + domain.getName())){
                return true;
            }
        }
        return false;
    }

}
