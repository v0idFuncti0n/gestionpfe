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
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy="university")
    @ToString.Exclude
    private List<Department> departments;

    public University(String name, List<Department> departments) {
        this.name = name;
        this.departments = departments;
    }
}
