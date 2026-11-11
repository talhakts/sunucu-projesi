package com.proje.sunucu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Kontrol listesi Madde 16, 17, 31 ve 32 geregi:
 * Kullanici (User) varligi.
 * Aktiflik bilgisi ve rol veritabaninda tutulur.
 * Parola alani Argon2id hash'ini barindiracak sekilde en az VARCHAR(255) olarak tanimlanmistir.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 100)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "role", nullable = false, length = 50)
    private String role; // "ROLE_ADMIN", "ROLE_DOCTOR"

    @Column(name = "active", nullable = false)
    private boolean active;

    @Column(name = "full_name", length = 150)
    private String fullName;

    // JPA zorunlu parametresiz constructor
    public User() {
    }

    public User(Long id, String username, String password, String role, boolean active, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
