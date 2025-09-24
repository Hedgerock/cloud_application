package com.hedgerock.app_server.service.security_service;

import com.hedgerock.app_server.TestUtils;
import com.hedgerock.app_server.entity.UserEntity;
import com.hedgerock.app_server.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class SecurityDetailsServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityDetailsService securityDetailsService;

    @Test
    @DisplayName("LoadUserByUsername test case when user not found")
    void givenEmail_whenLoadUserByUsername_thenThrowsUsernameNotFoundException() {
        //given
        final String email = TestUtils.getEmail();
        BDDMockito.given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.empty());
        //when then
        assertThatThrownBy(() -> securityDetailsService.loadUserByUsername(email))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining(String.format("User with email %s not found", email));

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(anyString());
        Mockito.verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("LoadUserByUsername test")
    void givenEmail_whenLoadUserByUsername_thenReturnUserDetails() {
        //given
        final UserEntity user = TestUtils.getUserEntity();
        final String email = TestUtils.getEmail();
        final var authorities = TestUtils.getAuthorities();

        BDDMockito.given(userRepository.findByEmail(anyString()))
                .willReturn(Optional.of(user));
        //when
        var result = securityDetailsService.loadUserByUsername(email);
        //then

        assertThat(result.getUsername()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo(user.getPassword());

        Assertions.assertThatCollection(result.getAuthorities()).hasSameSizeAs(authorities);
        assertThat(result.getAuthorities().iterator().next())
                .isEqualTo(authorities.getFirst());

        Mockito.verify(userRepository, Mockito.times(1))
                .findByEmail(anyString());

        Mockito.verifyNoMoreInteractions(userRepository);
    }
}
