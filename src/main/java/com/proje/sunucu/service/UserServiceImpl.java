package com.proje.sunucu.service;

import com.proje.sunucu.dto.UserDto;
import com.proje.sunucu.entity.User;
import com.proje.sunucu.exception.BadRequestException;
import com.proje.sunucu.exception.ResourceNotFoundException;
import com.proje.sunucu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontrol listesi Madde 5, 7, 20 ve 31 geregi:
 * Kullanici servisi implementasyonu.
 * Veri degistiren metotlar @Transactional ile Service katmaninda yonetilir.
 * Constructor injection kullanilmistir.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> dtos = new ArrayList<>();
        for (User user : users) {
            dtos.add(convertToDto(user));
        }
        return dtos;
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + id));
        return convertToDto(user);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Kullanici bulunamadi: " + username));
        return convertToDto(user);
    }

    @Override
    @Transactional
    public UserDto createUser(User user) {
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new BadRequestException("Kullanici adi bos olamaz.");
        }
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new BadRequestException("Bu kullanici adi zaten kayitli: " + user.getUsername());
        }
        user.setId(null);
        User saved = userRepository.save(user);
        return convertToDto(saved);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    @Transactional
    public void createInitialUserIfNotExists(String username, String encodedPassword, String role, String fullName) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = new User(
                    null,
                    username,
                    encodedPassword,
                    role,
                    true,
                    fullName
            );
            userRepository.save(user);
            System.out.println(">>> [UserService] Varsayilan kullanici olusturuldu: " + username);
        }
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                user.isActive(),
                user.getFullName()
        );
    }
}
