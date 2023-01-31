package com.gestionpfe.model;

import com.gestionpfe.enums.RendezvousState;
import jdk.jfr.Timestamp;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class Rendezvous {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String request;
    private String decliningMessage;

    @Timestamp
    private LocalDateTime rendezvous;

    @Enumerated(EnumType.STRING)
    private RendezvousState rendezvousState;

    @ManyToOne
    private StudentGroup studentGroup;

}
