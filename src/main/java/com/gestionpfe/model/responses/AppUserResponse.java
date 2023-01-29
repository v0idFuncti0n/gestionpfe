package com.gestionpfe.model.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
