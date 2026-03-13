package com.achlys20.EventBackend.Auth;

import com.achlys20.EventBackend.Auth.dto.AuthResponse;
import com.achlys20.EventBackend.Auth.dto.LoginRequest;
import com.achlys20.EventBackend.Auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse registerUser(@RequestBody RegisterRequest registerRequest){
        return authService.registerUser(registerRequest);
    }

    @PostMapping("/log-in")
    public AuthResponse loginUser(@RequestBody LoginRequest loginRequest){
        return authService.loginUser(loginRequest);
    }
}
