package com.proje.sunucu.controller;

import com.proje.sunucu.dto.UserDto;
import com.proje.sunucu.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontrol listesi Madde 3, 4, 7, 31 ve 35 geregi:
 * Kullanici REST Controller sinifi ve OpenAPI dokumantasyonu.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Kullanıcı Yönetimi (User)", description = "Sistem kullanıcılarını listeleme ve görüntüleme işlemleri")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Tüm kullanıcıları listele", description = "Sistemdeki aktif kullanıcıları ve rollerini döndürür (Parolalar gizlenmiştir).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Başarılı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Kimliğe göre kullanıcı getir", description = "Belirtilen ID'ye sahip kullanıcının profilini döndürür.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Kullanıcı bulundu"),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
