package com.achlys20.EventBackend.Auth;

import com.achlys20.EventBackend.Auth.dto.AuthResponse;
import com.achlys20.EventBackend.Auth.dto.LoginRequest;
import com.achlys20.EventBackend.Auth.dto.RegisterRequest;
import com.achlys20.EventBackend.Configs.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final OrganizerRepository organizerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse registerUser(RegisterRequest registerRequest) {

        System.out.println("registering user....");

        if (organizerRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("UserName Already Exists");
        }
        Organizer organizer = new Organizer();

        organizer.setName(registerRequest.getName());
        organizer.setEmail(registerRequest.getEmail());
        organizer.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        organizer.setCreatedAt(LocalDateTime.now());

        String token = jwtService.generateToken(organizer);

        organizerRepository.save(organizer);

        return new AuthResponse(token, organizer.getEmail(), organizer.getName());
    }

    public AuthResponse loginUser(LoginRequest loginRequest) {
        Organizer organizer = organizerRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("user does not exist"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), organizer.getPassword())) {
            throw new RuntimeException("Invalid UserName or Password");
        }

        String token = jwtService.generateToken(organizer);

        return new AuthResponse(token, organizer.getEmail(), organizer.getName());
    }
}
