package com.gestionpfe.model.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gestionpfe.enums.RendezvousState;
import com.gestionpfe.model.StudentGroup;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezvousResponse {

    // TODO: validations

    private Long id;

    private String request;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rendezvous;

    private RendezvousState rendezvousState;

    @JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
    @JsonIdentityReference(alwaysAsId=true)
    private StudentGroup studentGroup;
}
