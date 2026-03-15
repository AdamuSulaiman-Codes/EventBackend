package com.achlys20.EventBackend.Event;

import com.achlys20.EventBackend.Auth.Organizer;
import com.achlys20.EventBackend.Event.enums.EventStatus;
import com.achlys20.EventBackend.Event.enums.TicketType;
import com.achlys20.EventBackend.Registration.Registration;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;


    private String venue;

    private Integer capacity;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(unique = true, nullable = false)
    private String inviteToken;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    private BigDecimal ticketPrice;

    private String posterUrl;

    @Enumerated(EnumType.STRING)
    private EventStatus status;

    private LocalDateTime createdAt;

    private String PosterPublicId;

    @ManyToOne
    @JoinColumn(name = "organizer_id")
    private Organizer organizer;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private List<Registration> registrations;
}
