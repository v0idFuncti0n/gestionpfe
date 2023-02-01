package com.gestionpfe.model;

import com.gestionpfe.enums.StudentGroupState;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class StudentGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany(cascade = { CascadeType.REFRESH ,CascadeType.DETACH, CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(
            name = "student_studentGroup",
            joinColumns = { @JoinColumn(name = "student_group_id") },
            inverseJoinColumns = { @JoinColumn(name = "student_id") }
    )
    @ToString.Exclude
    private List<AppUser> students;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE})
    private PFESubject pfeSubject;

    @Enumerated(EnumType.STRING)
    private StudentGroupState studentGroupState;

    @OneToMany(mappedBy="studentGroup")
    private List<Rendezvous> rendezvous;

    private String driveUrl;

    private boolean isDriveUrlPublished;

    @Override
    public String toString() {
        StringBuilder studentGroupNames = new StringBuilder();
        getStudents().forEach(student -> {
            studentGroupNames.append(student.getFirstName())
                    .append(" ")
                    .append(student.getLastName())
                    .append(",");
        });
        studentGroupNames.deleteCharAt(studentGroupNames.length() - 1);
        return studentGroupNames.toString();
    }
}
