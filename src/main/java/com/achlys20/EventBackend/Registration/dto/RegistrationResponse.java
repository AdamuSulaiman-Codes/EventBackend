package com.achlys20.EventBackend.Registration.dto;


import com.achlys20.EventBackend.Registration.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponse {

    private Long id;  // auto-generated registration ID

    private String attendeeName;

    private String attendeeEmail;

    private PaymentStatus paymentStatus;

    private String paystackReference;

    private LocalDateTime registeredAt;

}