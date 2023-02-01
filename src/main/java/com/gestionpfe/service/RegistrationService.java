package com.gestionpfe.service;

import com.gestionpfe.email.EmailSender;
import com.gestionpfe.email.EmailService;
import com.gestionpfe.exceptions.TokenException;
import com.gestionpfe.exceptions.UserException;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.enums.AppUserRole;
import com.gestionpfe.model.requests.StudentRegistrationRequest;
import com.gestionpfe.model.requests.SupervisorRegistrationRequest;
import com.gestionpfe.security.validators.StudentEmailValidator;
import com.gestionpfe.model.ConfirmationToken;
import com.gestionpfe.security.token.ConfirmationTokenService;
import com.gestionpfe.security.validators.SupervisorEmailValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final StudentEmailValidator studentEmailValidator;
    private final SupervisorEmailValidator supervisorEmailValidator;
    private final ConfirmationTokenService confirmTokenService;
    private final EmailSender emailSender;


    public RegistrationService(AppUserService appUserService, StudentEmailValidator studentEmailValidator, SupervisorEmailValidator supervisorEmailValidator, ConfirmationTokenService confirmTokenService, EmailSender emailSender) {
        this.appUserService = appUserService;
        this.studentEmailValidator = studentEmailValidator;
        this.supervisorEmailValidator = supervisorEmailValidator;
        this.confirmTokenService = confirmTokenService;
        this.emailSender = emailSender;
    }

    public AppUser registerSupervisor(SupervisorRegistrationRequest request) {
        boolean isValidEmail = supervisorEmailValidator.test(request.getEmail());
        if (isValidEmail) {
            AppUser supervisor = new AppUser(request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getDepartment(),
                    AppUserRole.SUPERVISOR);
            String tokenForNewUser = appUserService.signUpUser(supervisor);

            //Since, we are running the spring boot application in localhost, we are hardcoding the
            //url of the server. We are creating a POST request with token param
            String link = "http://localhost:8090/api/v1/registration/confirm?token=" + tokenForNewUser;

            emailSender.sendEmail(request.getEmail(), EmailService.buildConfirmTokenEmail(request.getFirstName(), link), EmailService.CONFIRM_YOUR_EMAIL);
            return supervisor;
        } else {
            throw new UserException(String.format("Email %s, not valid", request.getEmail()));
        }
    }

    public AppUser registerStudent(StudentRegistrationRequest request) throws IllegalStateException {
        boolean isValidEmail = studentEmailValidator.test(request.getEmail());
        if (isValidEmail) {
            AppUser student = new AppUser(request.getCodeApogee(),
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPassword(),
                    request.getBranch(),
                    AppUserRole.STUDENT);
            String tokenForNewUser = appUserService.signUpUser(student);

            //Since, we are running the spring boot application in localhost, we are hardcoding the
            //url of the server. We are creating a POST request with token param
            String link = "http://localhost:8090/api/v1/registration/confirm?token=" + tokenForNewUser;

            emailSender.sendEmail(request.getEmail(), EmailService.buildConfirmTokenEmail(request.getFirstName(), link), EmailService.CONFIRM_YOUR_EMAIL);
            return student;
        } else {
            throw new UserException(String.format("Email %s, not valid", request.getEmail()));
        }
    }


    @Transactional
    public String confirmToken(String token) {
        Optional<ConfirmationToken> confirmToken = confirmTokenService.getToken(token);

        if (confirmToken.isEmpty()) {
            throw new TokenException("Token not found!");
        }

        if (confirmToken.get().getConfirmedAt() != null) {
            throw new TokenException("Email is already confirmed");
        }

        LocalDateTime expiresAt = confirmToken.get().getExpiresAt();

        if (expiresAt.isBefore(LocalDateTime.now())) {
            throw new TokenException("Token is already expired!");
        }

        confirmTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmToken.get().getAppUser().getEmail());

        //Returning confirmation message if the token matches
        return "Your email is confirmed. Thank you for using our service!";
    }
}
