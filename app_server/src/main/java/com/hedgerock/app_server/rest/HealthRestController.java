package com.hedgerock.app_server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthRestController {

    @GetMapping("ping")
    public ResponseEntity<?> checkHealth() {
        return ResponseEntity.ok().build();
    }

}
