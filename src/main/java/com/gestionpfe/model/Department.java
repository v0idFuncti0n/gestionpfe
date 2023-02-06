package com.gestionpfe.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="establishment_id", nullable=false)
    private Establishment establishment;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="department")
    @ToString.Exclude
    private List<Branch> branches;

    @OneToMany(mappedBy="department")
    @ToString.Exclude
    private List<AppUser> supervisors;

    public Department(String name, Establishment establishment) {
        this.name = name;
        this.establishment = establishment;
    }
}
