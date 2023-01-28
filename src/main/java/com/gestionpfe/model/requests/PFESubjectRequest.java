package com.gestionpfe.model.requests;

import com.gestionpfe.model.AppUser;
import com.gestionpfe.model.StudentGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PFESubjectRequest {

    // TODO: validations

    private Long supervisor;

    private String subject;

    private String description;

    private int groupNumber;

}
