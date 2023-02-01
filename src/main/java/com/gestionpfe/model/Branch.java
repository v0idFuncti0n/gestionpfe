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
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private Department department;

    @OneToMany(mappedBy="branch")
    @ToString.Exclude
    private List<AppUser> students;

    public Branch(String name, Department department, List<AppUser> students) {
        this.name = name;
        this.department = department;
        this.students = students;
    }
}
