package com.proje.sunucu.repository;

import com.proje.sunucu.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Kontrol listesi Madde 6 ve 31 geregi:
 * Kullanici veri erisim arayuzu (Spring Data arayuzleri kullanilmaz).
 */
public interface UserRepository {

    List<User> findAll();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User save(User user);

    User update(User user);

    void delete(User user);
}

