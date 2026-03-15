package com.achlys20.EventBackend.Registration;

import com.achlys20.EventBackend.Email.EmailService;
import com.achlys20.EventBackend.Event.Event;
import com.achlys20.EventBackend.Event.EventRepository;
import com.achlys20.EventBackend.Event.enums.EventStatus;
import com.achlys20.EventBackend.Event.enums.TicketType;
import com.achlys20.EventBackend.QrCode.QrCodeService;
import com.achlys20.EventBackend.Registration.dto.PublicEventResponse;
import com.achlys20.EventBackend.Registration.dto.RegistrationRequest;
import com.achlys20.EventBackend.Registration.dto.RegistrationResponse;
import com.achlys20.EventBackend.Registration.enums.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final QrCodeService qrCodeService;
    private final EmailService emailService;

    private PublicEventResponse mapToPublicResponse(Event event) {

        PublicEventResponse response = new PublicEventResponse();

        response.setTitle(event.getTitle());
        response.setDescription(event.getDescription());
        response.setVenue(event.getVenue());

        response.setStartTime(event.getStartTime());
        response.setEndTime(event.getEndTime());

        response.setCapacity(event.getCapacity());
        response.setPosterUrl(event.getPosterUrl());
        response.setTicketType(event.getTicketType());

        if (event.getTicketType() == TicketType.FREE) {
            response.setTicketPrice(BigDecimal.ZERO);
        } else {
            response.setTicketPrice(event.getTicketPrice());
        }

        response.setEventStatus(event.getStatus());

        return response;
    }

    private RegistrationResponse mapToResponse(Registration registration) {

        RegistrationResponse response = new RegistrationResponse();

        response.setId(registration.getId());
        response.setAttendeeName(registration.getAttendeeName());
        response.setAttendeeEmail(registration.getAttendeeEmail());
        response.setPaymentStatus(registration.getPaymentStatus());
        response.setPaystackReference(registration.getPaystackReference());
        response.setRegisteredAt(registration.getRegisteredAt());

        return response;
    }

    public List<RegistrationResponse> getEventRegistrations(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Registration> registrations = registrationRepository.findByEvent(event);

        return registrations.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public PublicEventResponse getEventByInviteToken(String inviteToken) {
        Event event = eventRepository.findByInviteToken(inviteToken)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getStatus() != EventStatus.OPEN) {
            throw new RuntimeException("Registration closed");
        }

        return mapToPublicResponse(event);
    }

    @Transactional
    public void register(String inviteToken, RegistrationRequest request) {

        Event event = eventRepository.findByInviteToken(inviteToken)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getStatus() != EventStatus.OPEN) {
            throw new RuntimeException("Registration closed");
        }

        long registrationCount = registrationRepository.countByEventId(event.getId());

        if (registrationCount >= event.getCapacity()) {
            throw new RuntimeException("Event is full");
        }

        boolean alreadyRegistered =
                registrationRepository.existsByEventIdAndAttendeeEmail(
                        event.getId(),
                        request.getAttendeeEmail()
                );

        if (alreadyRegistered) {
            throw new RuntimeException("You already registered for this event");
        }

        Registration registration = new Registration();

        registration.setEvent(event);
        registration.setAttendeeName(request.getAttendeeName());
        registration.setAttendeeEmail(request.getAttendeeEmail());
        registration.setPaymentStatus(PaymentStatus.FREE);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setTicketToken(inviteToken);
        registration.setCheckedIn(false);

        registrationRepository.save(registration);

        byte[] qrCode = qrCodeService.generateQrCode(inviteToken);

        emailService.sendTicketEmail(
                request.getAttendeeEmail(),
                request.getAttendeeName(),
                event.getTitle(),
                event.getStartTime(),
                qrCode
        );
;    }


}
