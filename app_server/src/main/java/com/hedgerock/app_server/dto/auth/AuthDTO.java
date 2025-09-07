package com.hedgerock.app_server.dto.auth;

import java.util.List;

public record AuthDTO(String username, List<String> authorities) {
}
