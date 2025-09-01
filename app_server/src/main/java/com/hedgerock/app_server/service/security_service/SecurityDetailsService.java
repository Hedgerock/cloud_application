package com.hedgerock.app_server.service.security_service;

import com.hedgerock.app_server.entity.AuthoritiesEntity;
import com.hedgerock.app_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email).map(user ->
                    User.builder()
                            .username(user.getEmail())
                            .password(user.getPassword())
                            .authorities(user.getAuthorities().stream()
                                    .map(AuthoritiesEntity::getAuthority)
                                    .map(SimpleGrantedAuthority::new).toList()
                            )
                            .build()
        ).orElseThrow(() ->
                        new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }
}
