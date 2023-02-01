package com.gestionpfe.model.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class SupervisorRegistrationRequest {

    @NotNull(message = "first name should not be null")
    @NotBlank(message = "first name is mandatory")
    @Size(min = 3, max = 60, message = "first name should be at least 3 characters long and should not exceed 60")
    private final String firstName;

    @NotNull(message = "last name should not be null")
    @NotBlank(message = "last name is mandatory")
    @Size(min = 3, max = 60, message = "last name should be at least 3 characters long and should not exceed 60")
    private final String lastName;

    @NotNull(message = "email should not be null")
    @NotBlank(message = "email is mandatory")
    @Size(min = 5, max = 60, message = "email should be at least 5 characters long and should not exceed 60")
    private final String email;

    @NotNull(message = "password should not be null")
    @NotBlank(message = "password is mandatory")
    @Size(min = 8, max = 60, message = "password should be at least 8 characters long and should not exceed 60")
    private final String password;

    @NotNull(message = "department should not be null")
    @NotBlank(message = "department is mandatory")
    private final Long departmentId;

}
