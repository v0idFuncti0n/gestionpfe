package com.gestionpfe.startup;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.AppUserRole;
import com.gestionpfe.model.Domain;
import com.gestionpfe.repository.AppUserRepository;
import com.gestionpfe.repository.DomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final DomainRepository domainRepository;
    private final AppUserRepository appUserRepository;

    @Autowired
    public CommandLineAppStartupRunner(PasswordEncoder passwordEncoder, DomainRepository domainRepository, AppUserRepository appUserRepository) {
        this.passwordEncoder = passwordEncoder;
        this.domainRepository = domainRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public void run(String... args) {

        createStudentDomain();
        createSupervisorDomain();

        createDefaultStudentUser();
        createDefaultSupervisorUsers();
    }

    private void createDefaultSupervisorUsers() {
        AppUser supervisor = new AppUser("Youssef",
                "Bajm3a",
                "youssef.bajm3a@uae.ac.ma",
                passwordEncoder.encode("password"),
                "Informatique",
                AppUserRole.SUPERVISOR,
                true);
        appUserRepository.save(supervisor);

        supervisor = new AppUser("Ahmed",
                "Bou3aza",
                "ahmed.bou3aza@uae.ac.ma",
                passwordEncoder.encode("password"),
                "Informatique",
                AppUserRole.SUPERVISOR,
                true);
        appUserRepository.save(supervisor);
    }

    private void createStudentDomain() {
        Domain domain = domainRepository.save(new Domain("@etu.uae.ac.ma", AppUserRole.STUDENT));
        domainRepository.save(domain);
    }

    private void createSupervisorDomain() {
        Domain domain = domainRepository.save(new Domain("@uae.ac.ma", AppUserRole.SUPERVISOR));
        domainRepository.save(domain);
    }

    public void createDefaultStudentUser() {
        AppUser student = new AppUser("170010060",
                "Anas",
                "Lakchouch",
                "anas.lakchouch@etu.uae.ac.ma",
                passwordEncoder.encode("password"),
                "M2I",
                AppUserRole.STUDENT,
                true);
        appUserRepository.save(student);
    }

}