package com.achlys20.EventBackend.Event;

import com.achlys20.EventBackend.Auth.Organizer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(Organizer organizer);
}
