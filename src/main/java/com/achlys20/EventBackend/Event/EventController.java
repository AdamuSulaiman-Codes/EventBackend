package com.achlys20.EventBackend.Event;

import com.achlys20.EventBackend.Event.dto.EventRequest;
import com.achlys20.EventBackend.Event.dto.EventResponse;
import com.achlys20.EventBackend.Registration.RegistrationService;
import com.achlys20.EventBackend.Registration.dto.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final RegistrationService registrationService;

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(
            Authentication authentication,
            @RequestPart("event") EventRequest eventRequest,
            @RequestPart("poster") MultipartFile poster
    ){
        eventService.createEvent(eventRequest, authentication.getName(), poster);
        return ResponseEntity.ok("EVENT CREATED");
    }
    @GetMapping("/my")
    public ResponseEntity<List<EventResponse>> getMyEvents(Authentication authentication){
        List<EventResponse> eventResponses = eventService.getMyEvent(authentication.getName());
        return ResponseEntity.ok(eventResponses);
    }
    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getSingleEvent(@PathVariable Long eventId){
        EventResponse eventResponse = eventService.getSingleEvent(eventId);
        return ResponseEntity.ok(eventResponse);
    }
    @DeleteMapping("/{eventId}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventId){
        eventService.deleteEvent(eventId);
        return ResponseEntity.ok("EVENT DELETED");
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<String> updateEvent(
            @PathVariable Long eventId,
            @RequestBody EventRequest eventRequest,
            @RequestPart("poster") MultipartFile poster
    ){
        eventService.updateEvent(eventId, eventRequest, poster);
        return ResponseEntity.ok("EVENT DELETED");
    }

    @PatchMapping("/{eventId}/status")
    public ResponseEntity<String> updateEvednt(@PathVariable Long eventId){
        eventService.updateEventStatus(eventId);
        return ResponseEntity.ok("EVENT DELETED");
    }

    @GetMapping("/{eventId/registrations}")
    public ResponseEntity<List<RegistrationResponse>> getEventRegistrationDetails(@PathVariable Long eventId){
        List<RegistrationResponse> registrationResponses = registrationService.getEventRegistrations(eventId);
        return ResponseEntity.ok(registrationResponses);
    }
    @GetMapping("/{eventId/registrations}/export")
    public ResponseEntity<byte[]> exportRegistration(@PathVariable Long eventId){
        List<RegistrationResponse> registrationResponses = registrationService.getEventRegistrations(eventId);
        StringBuilder csv = new StringBuilder();
        csv.append("Name,Email,Payment Status,Reference,Registered At\n");

        for (RegistrationResponse r : registrationResponses) {
            csv.append(r.getAttendeeName()).append(",")
                    .append(r.getAttendeeEmail()).append(",")
                    .append(r.getPaymentStatus()).append(",")
                    .append(r.getPaystackReference()).append(",")
                    .append(r.getRegisteredAt()).append("\n");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=registrations.csv")
                .body(csv.toString().getBytes());
    }
}
