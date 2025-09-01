package com.hedgerock.app_server.rest;

import com.hedgerock.app_server.dto.users.CurrentUserDTO;
import com.hedgerock.app_server.dto.users.UserDTO;
import com.hedgerock.app_server.service.user_service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity
                .ok(userService.getAll());
    }

    @GetMapping("{id:\\d+}")
    public ResponseEntity<CurrentUserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity
                .ok(userService.getById(id));
    }
}
