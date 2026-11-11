package com.proje.sunucu.dto;

/**
 * proje.md ve baslangic.md geregi:
 * Kullanici verilerini istemciye guvenle iletmek icin kullanilan DTO.
 * Parola alani guvenlik amaciyla DTO'da yer almaz.
 */
public class UserDto {

    private Long id;
    private String username;
    private String role;
    private boolean active;
    private String fullName;

    public UserDto() {
    }

    public UserDto(Long id, String username, String role, boolean active, String fullName) {
        this.id = id;
        this.username = username;
        this.role = role;
        this.active = active;
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
