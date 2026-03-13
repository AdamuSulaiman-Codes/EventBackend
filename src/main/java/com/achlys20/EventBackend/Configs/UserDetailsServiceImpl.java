package com.achlys20.EventBackend.Configs;




import com.achlys20.EventBackend.Auth.Organizer;
import com.achlys20.EventBackend.Auth.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final OrganizerRepository organizerRepository;
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Organizer organizer = organizerRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Organizer not found")
                );

        return new org.springframework.security.core.userdetails.User(
                organizer.getEmail(),
                organizer.getPassword(),
                List.of() // no roles needed
        );
    }
}
