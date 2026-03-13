package com.achlys20.EventBackend.Ticket;

import com.achlys20.EventBackend.Registration.Registration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    @JoinColumn(name = "registration_id", unique = true, nullable = false)
    private Registration registration;

    private String qrCodeUrl;

    private boolean checkedIn = false;

    private LocalDateTime checkedInAt;
}
