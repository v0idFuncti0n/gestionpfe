package com.gestionpfe.model.responses;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gestionpfe.enums.StudentGroupState;
import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.PFESubject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentGroupResponse {
    // TODO: validations
    private Long id;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private List<AppUser> students;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private PFESubject pfeSubject;

    private StudentGroupState studentGroupState;

    private String driveUrl;

    private boolean isDriveUrlPublished;
}
