package com.proje.sunucu.security;

import com.proje.sunucu.entity.User;
import com.proje.sunucu.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Kontrol listesi Madde 7 ve 31 geregi:
 * Kimlik dogrulama veritabanindaki kullanici bilgileri uzerinden yapilir.
 * Constructor injection kullanilmistir.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Kullanici bulunamadi: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isActive(), // enabled
                true,            // accountNonExpired
                true,            // credentialsNonExpired
                true,            // accountNonLocked
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole()))
        );
    }
}

