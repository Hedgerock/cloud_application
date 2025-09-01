package com.hedgerock.app_server.dto;

import com.hedgerock.app_server.entity.UserEntity;

public record UserBodyDTO(
        String email,
        String password
) {

    public UserEntity toEntity() {
        return UserEntity.builder()
                .email(email)
                .password(password)
                .build();
    }

}
