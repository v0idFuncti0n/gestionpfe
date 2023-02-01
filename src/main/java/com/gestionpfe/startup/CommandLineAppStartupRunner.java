package com.gestionpfe.startup;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.exceptions.DepartmentException;
import com.gestionpfe.exceptions.UniversityException;
import com.gestionpfe.model.*;
import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class CommandLineAppStartupRunner implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;

    private final DomainRepository domainRepository;
    private final AppUserRepository appUserRepository;
    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;

    @Autowired
    public CommandLineAppStartupRunner(PasswordEncoder passwordEncoder, DomainRepository domainRepository, AppUserRepository appUserRepository, UniversityRepository universityRepository, DepartmentRepository departmentRepository, BranchRepository branchRepository) {
        this.passwordEncoder = passwordEncoder;
        this.domainRepository = domainRepository;
        this.appUserRepository = appUserRepository;
        this.universityRepository = universityRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
    }

    @Override
    public void run(String... args) {
        createUniversities();

        createDepartments();

        createBranches();

        createStudentDomain();
        createSupervisorDomain();

        createDefaultStudentUser();
        createDefaultSupervisorUsers();
    }

    public void createUniversities() {
        University ams = new University("Abdelmalek Essaadi", new ArrayList<>());
        universityRepository.save(ams);
    }
    private void createDepartments() {
        University ams = universityRepository.findUniversityByName("Abdelmalek Essaadi").orElseThrow(() -> {
            throw new UniversityException("university not found");
        });
        Department departmentInformatique = new Department("Informatique", ams);
        departmentRepository.save(departmentInformatique);
    }

    private void createBranches() {
        Department departmentInformatique = departmentRepository.findDepartmentByName("Informatique").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });
        Branch smi = new Branch("SMI", departmentInformatique, new ArrayList<>());
        List<Branch> branches = new ArrayList<>(departmentInformatique.getBranches());
        branches.add(smi);
        departmentInformatique.setBranches(branches);
        branchRepository.save(smi);
        departmentRepository.save(departmentInformatique);
    }

    private void createDefaultSupervisorUsers() {
        Department departmentInformatique = departmentRepository.findDepartmentByName("Informatique").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });
        AppUser supervisor = new AppUser("Youssef",
                "Bajm3a",
                "lakchouchanas@gmail.com",
                passwordEncoder.encode("password"),
                departmentInformatique,
                AppUserRole.SUPERVISOR,
                true);
        appUserRepository.save(supervisor);

        supervisor = new AppUser("Ahmed",
                "Bou3aza",
                "ahmed.bou3aza@uae.ac.ma",
                passwordEncoder.encode("password"),
                departmentInformatique,
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
        Branch smi = branchRepository.findBranchByName("SMI").orElseThrow(() -> {
            throw new BranchException("branch not found");
        });
        log.error("{}", smi);
        AppUser student = new AppUser("170010060",
                "Anas",
                "Lakchouch",
                "anas.lakchouch@etu.uae.ac.ma",
                passwordEncoder.encode("password"),
                smi,
                AppUserRole.STUDENT,
                true);
        appUserRepository.save(student);
    }

}