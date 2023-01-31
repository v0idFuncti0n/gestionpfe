package com.gestionpfe.model.requests.rendezvous;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezvousStudentDeclineRequest {

    // TODO: validations
    private String decliningMessage;
}
