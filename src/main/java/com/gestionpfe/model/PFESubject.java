package com.gestionpfe.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class PFESubject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private AppUser supervisor;

    private String subject;

    private String description;

    private int groupNumber;

    private boolean published;

    @OneToMany(mappedBy="pfeSubject")
    @ToString.Exclude
    private List<StudentGroup> studentGroups;
}
