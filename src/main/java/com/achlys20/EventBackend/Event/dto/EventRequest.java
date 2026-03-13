package com.achlys20.EventBackend.Event.dto;

import com.achlys20.EventBackend.Event.enums.EventStatus;
import com.achlys20.EventBackend.Event.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    private String title;

    private String description;

    private LocalDateTime date;

    private String venue;

    private Integer capacity;

    private TicketType ticketType;

    private BigDecimal ticketPrice;

    private EventStatus status;
}
