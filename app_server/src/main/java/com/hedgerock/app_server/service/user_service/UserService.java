package com.hedgerock.app_server.service.user_service;

import com.hedgerock.app_server.dto.CurrentUserDTO;
import com.hedgerock.app_server.dto.UserDTO;

import java.util.List;

public interface UserService {
    CurrentUserDTO getById(Long id);
    List<UserDTO> getAll();
}
