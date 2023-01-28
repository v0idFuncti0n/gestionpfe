package com.gestionpfe.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Domain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    // this field will take the same roles value to determine whether this domain belongs to STUDENT or SUPERVISOR
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole;

    public Domain(String name, AppUserRole appUserRole) {
        this.name = name;
        this.appUserRole = appUserRole;
    }
}
