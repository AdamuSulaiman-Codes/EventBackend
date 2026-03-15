package com.achlys20.EventBackend.Registration;

import com.achlys20.EventBackend.Event.Event;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByEvent(Event event);

    boolean existsByEventIdAndAttendeeEmail(Long id, @Email @NotBlank String attendeeEmail);

    long countByEventId(Long id);
}
