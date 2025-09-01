package com.hedgerock.app_server.dto.users;

import com.hedgerock.app_server.entity.UserEntity;
import lombok.Builder;

@Builder
public record UserDTO(
        Long id,
        String email,
        Long diskSpace,
        Long usedSpace
) {

    public static UserDTO toDTO(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .diskSpace(entity.getDiskSpace())
                .usedSpace(entity.getUsedSpace())
                .build();
    }

}
