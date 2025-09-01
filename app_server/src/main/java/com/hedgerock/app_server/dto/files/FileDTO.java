package com.hedgerock.app_server.dto.files;

import com.hedgerock.app_server.entity.FileEntity;
import lombok.Builder;

@Builder
public record FileDTO(
        Long id,
        String name
) {

    public static FileDTO toDTO(FileEntity fileEntity) {
        return FileDTO
                .builder()
                .id(fileEntity.getId())
                .name(fileEntity.getName())
                .build();
    }

}
