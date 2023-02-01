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
    @JoinColumn(name="university_id", nullable=false)
    private University university;

    @OneToMany(fetch = FetchType.EAGER, mappedBy="department")
    @ToString.Exclude
    private List<Branch> branches;

    @OneToMany(mappedBy="department")
    @ToString.Exclude
    private List<AppUser> supervisors;

    public Department(String name, University university) {
        this.name = name;
        this.university = university;
    }
}
