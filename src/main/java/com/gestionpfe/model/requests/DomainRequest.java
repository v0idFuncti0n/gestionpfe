package com.gestionpfe.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestionpfe.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class DomainRequest {

    @NotNull(message = "name should not be null")
    @NotBlank(message = "name is mandatory")
    @Size(min = 3, max = 20, message = "name should be at least 3 characters long and should not exceed 20")
    private final String name;

    @NotNull(message = "role should not be null")
    @Enumerated(EnumType.STRING)
    @JsonProperty("role")
    private final AppUserRole appUserRole;
}
