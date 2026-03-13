package com.achlys20.EventBackend.Registration.dto;

import com.achlys20.EventBackend.Registration.enums.PaymentStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {

    @NotNull(message = "Event ID is required")
    private Long eventId;  // which event the registration is for

    @NotBlank(message = "Attendee name is required")
    private String attendeeName;

    @Email(message = "Attendee email must be valid")
    @NotBlank(message = "Attendee email is required")
    private String attendeeEmail;

    @NotNull(message = "Payment status is required")
    private PaymentStatus paymentStatus; // e.g., PAID, PENDING, FAILED

    private String paystackReference; // optional, only if payment is done
}
