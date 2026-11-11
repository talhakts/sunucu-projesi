package com.proje.sunucu.exception;

/**
 * Kontrol listesi Madde 28 ve 29 geregi:
 * Bulunamayan kayitlar icin firlatilan ve 404 donduren custom exception.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}

