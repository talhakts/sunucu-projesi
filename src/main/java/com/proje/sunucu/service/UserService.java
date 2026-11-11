package com.proje.sunucu.service;

import com.proje.sunucu.dto.UserDto;
import com.proje.sunucu.entity.User;

import java.util.List;

/**
 * Kontrol listesi Madde 5 ve 31 geregi:
 * Kullanici is mantigi arayuzu.
 */
public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUserById(Long id);

    UserDto getUserByUsername(String username);

    UserDto createUser(User user);
}
