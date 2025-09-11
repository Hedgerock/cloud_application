package com.hedgerock.app_server.dto.auth;

import com.hedgerock.app_server.entity.UserEntity;
import lombok.Builder;

@Builder
public record LoginDTO(String email, String password) {

    public static LoginDTO fromEntity(UserEntity user) {
        return LoginDTO.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .build();
    }

}
