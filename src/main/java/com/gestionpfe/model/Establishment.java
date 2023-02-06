package com.gestionpfe.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Establishment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy= "establishment")
    @ToString.Exclude
    private List<Department> departments;

    @ManyToOne
    @JoinColumn(name="university_id", nullable=false)
    private University university;

    public Establishment(String name, University university) {
        this.name = name;
        this.university = university;
        this.departments = new ArrayList<>();
    }

    public Establishment(String name, List<Department> departments) {
        this.name = name;
        this.departments = departments;
    }
}
