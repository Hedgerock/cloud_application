package com.hedgerock.app_server.dto.users;

import com.hedgerock.app_server.dto.files.FileDTO;
import com.hedgerock.app_server.entity.UserEntity;
import lombok.Builder;

import java.util.List;

@Builder
public record CurrentUserDTO(
        Long id,
        String email,
        Long diskSpace,
        Long usedSpace,
        List<FileDTO> files
) {

    public static CurrentUserDTO toDTO(UserEntity user) {
        return CurrentUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .diskSpace(user.getDiskSpace())
                .usedSpace(user.getUsedSpace())
                .files(user.getFiles().stream().map(FileDTO::toDTO).toList())
                .build();
    }

}
