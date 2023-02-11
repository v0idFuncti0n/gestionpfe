package com.gestionpfe.startup;

import com.gestionpfe.exceptions.BranchException;
import com.gestionpfe.exceptions.DepartmentException;
import com.gestionpfe.exceptions.EstablishmentException;
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
    private final EstablishmentRepository establishmentRepository;
    private final DepartmentRepository departmentRepository;
    private final BranchRepository branchRepository;
    private final UniversityRepository universityRepository;

    @Autowired
    public CommandLineAppStartupRunner(PasswordEncoder passwordEncoder, DomainRepository domainRepository, AppUserRepository appUserRepository, EstablishmentRepository establishmentRepository, DepartmentRepository departmentRepository, BranchRepository branchRepository, UniversityRepository universityRepository) {
        this.passwordEncoder = passwordEncoder;
        this.domainRepository = domainRepository;
        this.appUserRepository = appUserRepository;
        this.establishmentRepository = establishmentRepository;
        this.departmentRepository = departmentRepository;
        this.branchRepository = branchRepository;
        this.universityRepository = universityRepository;
    }

    @Override
    public void run(String... args) {
        createUniversities();

        createEstablishments();

        createDepartments();

        createBranches();

        createStudentDomain();
        createSupervisorDomain();

        createDefaultStudentUser();
        createDefaultSupervisorUsers();
        createDefaultAdminUsers();
    }

    public void createUniversities() {
        University ams = new University("Abdelmalek Essaadi", new ArrayList<>());
        universityRepository.save(ams);

        University mvu = new University("Mohammed V University", new ArrayList<>());
        universityRepository.save(mvu);
    }

    public void createEstablishments() {
        University ams = universityRepository.findUniversityByName("Abdelmalek Essaadi").orElseThrow(() -> {
            throw new EstablishmentException("university not found");
        });

        University mvu = universityRepository.findUniversityByName("Mohammed V University").orElseThrow(() -> {
            throw new EstablishmentException("university not found");
        });

        Establishment fst = new Establishment("Faculte des sciences", ams);
        establishmentRepository.save(fst);

        Establishment est = new Establishment("EST", mvu);
        establishmentRepository.save(est);
    }
    private void createDepartments() {
        Establishment fst = establishmentRepository.findEstablishmentByName("Faculte des sciences").orElseThrow(() -> {
            throw new EstablishmentException("establishment not found");
        });

        Establishment est = establishmentRepository.findEstablishmentByName("EST").orElseThrow(() -> {
            throw new EstablishmentException("establishment not found");
        });

        Department departmentInformatique = new Department("Informatique", fst);
        Department departmentMath = new Department("Math", fst);
        Department departmentPhys = new Department("Physique", fst);

        departmentRepository.save(departmentInformatique);
        departmentRepository.save(departmentPhys);
        departmentRepository.save(departmentMath);

        Department departmentInformatiqueEST = new Department("Informatique EST", est);
        departmentRepository.save(departmentInformatiqueEST);
    }

    private void createBranches() {
        Department departmentInformatique = departmentRepository.findDepartmentByName("Informatique").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });

        Department departmentMath = departmentRepository.findDepartmentByName("Math").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });

        Department departmentPhys = departmentRepository.findDepartmentByName("Physique").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });

        Department departmentInformatiqueEST = departmentRepository.findDepartmentByName("Informatique EST").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });

        Branch smi = new Branch("SMI", departmentInformatique, new ArrayList<>());
        List<Branch> branches = departmentInformatique.getBranches();
        branches.add(smi);
        departmentInformatique.setBranches(branches);
        branchRepository.save(smi);

        Branch sma = new Branch("SMA", departmentMath, new ArrayList<>());
        branches = departmentMath.getBranches();
        branches.add(sma);
        branchRepository.save(sma);

        Branch smp = new Branch("SMP", departmentPhys, new ArrayList<>());
        branches = departmentPhys.getBranches();
        branches.add(smp);
        branchRepository.save(smp);

        Branch smc = new Branch("SMC", departmentPhys, new ArrayList<>());
        branches = departmentPhys.getBranches();
        branches.add(smc);
        branchRepository.save(smc);

        Branch gl = new Branch("GL", departmentPhys, new ArrayList<>());
        branches = departmentInformatiqueEST.getBranches();
        branches.add(gl);
        branchRepository.save(gl);

        departmentRepository.save(departmentInformatique);
        departmentRepository.save(departmentPhys);
        departmentRepository.save(departmentMath);
        departmentRepository.save(departmentInformatiqueEST);
    }

    private void createDefaultSupervisorUsers() {
        Department departmentInformatique = departmentRepository.findDepartmentByName("Informatique").orElseThrow(() -> {
            throw new DepartmentException("department not found");
        });
        Department departmentInformatiqueEST = departmentRepository.findDepartmentByName("Informatique EST").orElseThrow(() -> {
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

        supervisor = new AppUser("Ahmed",
                "Boo",
                "ahmed.boo@uae.ac.ma",
                passwordEncoder.encode("password"),
                departmentInformatiqueEST,
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

        log.error("{}", smi);
        AppUser student2 = new AppUser("170010061",
                "Moad",
                "Zabt",
                "moad.zabt@etu.uae.ac.ma",
                passwordEncoder.encode("password"),
                smi,
                AppUserRole.STUDENT,
                true);
        appUserRepository.save(student2);

        AppUser student3 = new AppUser("170010062",
                "Moahmmed Anass",
                "Boukhancha",
                "mohammed.anass.boukhancha@etu.uae.ac.ma",
                passwordEncoder.encode("password"),
                smi,
                AppUserRole.STUDENT,
                true);
        appUserRepository.save(student3);
    }

    private void createDefaultAdminUsers() {
        AppUser admin = new AppUser("Admin",
                "TEST",
                "admin@admin.com",
                passwordEncoder.encode("password"),
                AppUserRole.ADMIN,
                true);
        appUserRepository.save(admin);
    }

}