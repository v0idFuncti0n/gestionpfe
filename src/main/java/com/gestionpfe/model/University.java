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

    @OneToMany(mappedBy= "university")
    @ToString.Exclude
    private List<Establishment> establishments;

    public University(String name, List<Establishment> establishments) {
        this.name = name;
        this.establishments = establishments;
    }
}
