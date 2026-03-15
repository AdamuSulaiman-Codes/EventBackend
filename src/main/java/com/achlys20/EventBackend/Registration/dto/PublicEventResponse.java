package com.achlys20.EventBackend.Registration.dto;

import com.achlys20.EventBackend.Event.enums.EventStatus;
import com.achlys20.EventBackend.Event.enums.TicketType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicEventResponse {

    private String title;

    private String description;

    private String venue;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer capacity;

    private String posterUrl;

    private TicketType ticketType;

    private EventStatus eventStatus;

    private BigDecimal ticketPrice;
}