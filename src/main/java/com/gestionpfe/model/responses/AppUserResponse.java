package com.gestionpfe.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestionpfe.enums.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String accessToken;
    @Enumerated(EnumType.STRING)
    @JsonProperty("roles")
    private AppUserRole appUserRole;
}
