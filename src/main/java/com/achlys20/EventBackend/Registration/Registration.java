package com.achlys20.EventBackend.Registration;

import com.achlys20.EventBackend.Event.Event;
import com.achlys20.EventBackend.Registration.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "registrations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id","attendee_email"}))
public class Registration {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(nullable = false)
    private String attendeeName;

    @Column(name = "attendee_email", nullable = false)
    private String attendeeEmail;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paystackReference;

    private LocalDateTime registeredAt;
}