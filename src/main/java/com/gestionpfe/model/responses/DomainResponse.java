package com.gestionpfe.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gestionpfe.model.AppUserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DomainResponse {

    private Long id;

    private String name;

    @JsonProperty("role")
    private AppUserRole appUserRole;
}
