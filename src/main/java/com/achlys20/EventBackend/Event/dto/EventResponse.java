package com.achlys20.EventBackend.Event.dto;

import com.achlys20.EventBackend.Event.enums.EventStatus;
import com.achlys20.EventBackend.Event.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private Long id;

    private String title;

    private String description;

    private LocalDateTime date;

    private String venue;

    private Integer capacity;

    private String slug;

    private String inviteToken;

    private TicketType ticketType;

    private BigDecimal ticketPrice;

    private String posterUrl;

    private EventStatus status;

    private LocalDateTime createdAt;
}
