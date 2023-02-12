package com.gestionpfe.model.requests.rendezvous;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gestionpfe.enums.RendezvousState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DateFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RendezvousSupervisorRequest {

    // TODO: validations

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private LocalDateTime rendezvous;

}
