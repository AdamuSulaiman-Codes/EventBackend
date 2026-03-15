package com.achlys20.EventBackend.Event;

import com.achlys20.EventBackend.Auth.Organizer;
import com.achlys20.EventBackend.Auth.OrganizerRepository;
import com.achlys20.EventBackend.Cloudinary.CloudinaryImage;
import com.achlys20.EventBackend.Cloudinary.CloudinaryService;
import com.achlys20.EventBackend.Event.dto.EventRequest;
import com.achlys20.EventBackend.Event.dto.EventResponse;
import com.achlys20.EventBackend.Event.enums.EventStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final OrganizerRepository organizerRepository;
    private final CloudinaryService cloudinaryService;

    // ---------------- Helper: Map Event to Response DTO ----------------
    private EventResponse mapToResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getVenue(),
                event.getCapacity(),
                event.getSlug(),
                event.getInviteToken(),
                event.getTicketType(),
                event.getTicketPrice(),
                event.getPosterUrl(),
                event.getStatus(),
                event.getCreatedAt()
        );
    }

    // ---------------- Create Event ----------------
    @Transactional
    public void createEvent(EventRequest eventRequest, String email, MultipartFile posterImage) {

        Organizer organizer = organizerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Organizer does not exist"));

        CloudinaryImage cloudinaryImage = cloudinaryService.uploadImage(posterImage);

        Event event = new Event();

        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setVenue(eventRequest.getVenue());
        event.setCapacity(eventRequest.getCapacity());
        event.setTicketType(eventRequest.getTicketType());
        event.setTicketPrice(eventRequest.getTicketPrice());
        event.setStatus(eventRequest.getStatus());

        // store both URL and public_id for future replacement
        event.setPosterUrl(cloudinaryImage.getUrl());
        event.setPosterPublicId(cloudinaryImage.getPublicId());

        event.setCreatedAt(LocalDateTime.now());
        event.setInviteToken(UUID.randomUUID().toString());

        String slug = eventRequest.getTitle()
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-");

        event.setSlug(slug + "-" + UUID.randomUUID().toString().substring(0,6));

        event.setOrganizer(organizer);

        eventRepository.save(event);
    }

    // ---------------- Get My Events ----------------
    public List<EventResponse> getMyEvent(String email) {
        Organizer organizer = organizerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        return eventRepository.findByOrganizer(organizer)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ---------------- Get Single Event ----------------
    public EventResponse getSingleEvent(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        return mapToResponse(event);
    }

    // ---------------- Delete Event ----------------
    @Transactional
    public void deleteEvent(Long eventId, String email) {
        Organizer organizer = organizerRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // delete poster from Cloudinary
        if (event.getPosterPublicId() != null) {
            cloudinaryService.deleteImage(event.getPosterPublicId());
        }

        eventRepository.delete(event);
    }

    // ---------------- Update Event ----------------
    @Transactional
    public void updateEvent(Long eventId, EventRequest eventRequest, MultipartFile poster) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setTitle(eventRequest.getTitle());
        event.setDescription(eventRequest.getDescription());
        event.setStartTime(eventRequest.getStartTime());
        event.setEndTime(eventRequest.getEndTime());
        event.setVenue(eventRequest.getVenue());
        event.setCapacity(eventRequest.getCapacity());
        event.setTicketType(eventRequest.getTicketType());
        event.setTicketPrice(eventRequest.getTicketPrice());
        event.setStatus(eventRequest.getStatus());

        if (poster != null && !poster.isEmpty()) {
            // delete old image
            if (event.getPosterPublicId() != null) {
                cloudinaryService.deleteImage(event.getPosterPublicId());
            }

            // upload new image
            CloudinaryImage newImage = cloudinaryService.uploadImage(poster);
            event.setPosterUrl(newImage.getUrl());
            event.setPosterPublicId(newImage.getPublicId());
        }

        // @Transactional ensures automatic update — no need to call save
    }

    // ---------------- Update Event Status ----------------
    @Transactional
    public void updateEventStatus(Long eventId) {

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getStatus() == EventStatus.OPEN) {
            event.setStatus(EventStatus.CLOSED);
        } else {
            event.setStatus(EventStatus.OPEN);
        }

        // @Transactional ensures automatic update
    }
}