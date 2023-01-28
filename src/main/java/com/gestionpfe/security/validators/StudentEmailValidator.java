package com.gestionpfe.security.validators;

import com.gestionpfe.model.AppUserRole;
import com.gestionpfe.model.Domain;
import com.gestionpfe.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class StudentEmailValidator implements Predicate<String> {

    private final String STUDENT_EMAIL_PATTERN = "\\w+([-+.']\\w+)*";
    private final DomainRepository domainRepository;

    @Autowired
    public StudentEmailValidator(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    @Override
    public boolean test(String email) {
        List<Domain> studentDomains = domainRepository.findByAppUserRole(AppUserRole.STUDENT);
        for(Domain domain : studentDomains) {
            if(email.matches(STUDENT_EMAIL_PATTERN + domain.getName())){
                return true;
            }
        }
        return false;
    }

}
