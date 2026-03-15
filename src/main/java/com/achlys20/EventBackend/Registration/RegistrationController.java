package com.achlys20.EventBackend.Registration;

import com.achlys20.EventBackend.Registration.dto.PublicEventResponse;
import com.achlys20.EventBackend.Registration.dto.RegistrationRequest;
import com.achlys20.EventBackend.Registration.dto.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/register")
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/{inviteToken}")
    public ResponseEntity<PublicEventResponse> getEventDetails(@PathVariable String inviteToken){
        PublicEventResponse publicEventResponse = registrationService.getEventByInviteToken(inviteToken);
        return ResponseEntity.ok(publicEventResponse);
    }

    @PostMapping("/{inviteToken}")
    public ResponseEntity<String> registerForEvent(
            @PathVariable String inviteToken,
            @RequestBody RegistrationRequest request
    ) {
        registrationService.register(inviteToken, request);
        return ResponseEntity.ok("USER REGISTERED TO EVENT");
    }
}
