package com.gestionpfe.model.requests.rendezvous;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestionpfe.enums.RendezvousState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezvousStudentRequest {

    // TODO: validations
    private String request;
}
