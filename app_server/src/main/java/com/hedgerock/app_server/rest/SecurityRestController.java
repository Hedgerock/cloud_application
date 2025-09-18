package com.hedgerock.app_server.rest;

import com.hedgerock.app_server.config.constraints.types.TokenType;
import com.hedgerock.app_server.dto.auth.AuthDTO;
import com.hedgerock.app_server.dto.auth.LoginDTO;
import com.hedgerock.app_server.dto.auth.RegisterDTO;
import com.hedgerock.app_server.dto.auth.RestorePasswordDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationPasswordTokenDTO;
import com.hedgerock.app_server.dto.auth.validation.ValidationTokenDTO;
import com.hedgerock.app_server.dto.response.SimpleResponseDTO;
import com.hedgerock.app_server.exceptions.CurrentBindException;
import com.hedgerock.app_server.service.email_service.EmailService;
import com.hedgerock.app_server.service.user_service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.hedgerock.app_server.rest.utils.RestUtils.getToken;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class SecurityRestController {

    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<SimpleResponseDTO> registerUser(@Valid @RequestBody RegisterDTO registerDTO,
                                                          BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CurrentBindException("Failed to register", bindingResult);
        }

        final String token = getToken();

        emailService.sendConfirmationEmail(registerDTO.email(), token, registerDTO);
        userService.cachePendingUser(
                TokenType.CONFIRM_EMAIL, token, registerDTO.getEncryptedDTO(passwordEncoder)
        );

        SimpleResponseDTO simpleResponseDTO =
                new SimpleResponseDTO(
                        String.format("Registration request for user %s has successfully created", registerDTO.email()));

        return ResponseEntity.ok(simpleResponseDTO);
    }

    @PostMapping("/restore_password")
    public ResponseEntity<SimpleResponseDTO> restorePassword(
            @Valid @RequestBody RestorePasswordDTO restorePasswordDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CurrentBindException("Wrong email", bindingResult);
        }

        final String token = getToken();
        emailService.sendRestorePasswordMessage(restorePasswordDTO.email(), token);
        userService.cachePendingEmailForRestore(TokenType.RESTORE_PASSWORD, token, restorePasswordDTO.email());

        SimpleResponseDTO simpleResponseDTO =
                new SimpleResponseDTO(
                        String.format("Email to %s has been sent", restorePasswordDTO.email()));

        return ResponseEntity.ok(simpleResponseDTO);
    }

    @PostMapping("/confirm_new_password")
    public ResponseEntity<SimpleResponseDTO> confirmPassword(
            @Valid @RequestBody ValidationPasswordTokenDTO validationPasswordTokenDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CurrentBindException("Invalid changing password details", bindingResult);
        }

        userService.confirmPassword(TokenType.RESTORE_PASSWORD, validationPasswordTokenDTO);

        SimpleResponseDTO simpleResponseDTO =
                new SimpleResponseDTO("Password has successfully changed");

        return ResponseEntity.ok(simpleResponseDTO);
    }

    @PostMapping("/confirm_email")
    public ResponseEntity<SimpleResponseDTO> confirmEmail(
            @Valid @RequestBody ValidationTokenDTO tokenDTO,
   BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CurrentBindException("Invalid token", bindingResult);
        }

        userService.confirmUser(TokenType.CONFIRM_EMAIL, tokenDTO.token());

        SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO("User has successfully confirmed and created");
        return ResponseEntity.ok(simpleResponseDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<SimpleResponseDTO> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession != null) {
            httpSession.invalidate();
        }

        SecurityContextHolder.clearContext();

        SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO("Successfully logout");
        return ResponseEntity.ok(simpleResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<SimpleResponseDTO> login(
            @Valid @RequestBody LoginDTO loginValue,
            BindingResult bindingResult,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) {
            throw new CurrentBindException("Invalid login credentials", bindingResult);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginValue.email(), loginValue.password());

        Authentication auth = authenticationManager.authenticate(authenticationToken);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

        SimpleResponseDTO simpleResponseDTO = new SimpleResponseDTO("Successfully logged in");
        return ResponseEntity.ok(simpleResponseDTO);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        var principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            List<String> authorities = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            var dto = new AuthDTO(userDetails.getUsername(), authorities);
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.ok(principal);
    }

}
