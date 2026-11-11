package com.proje.sunucu.exception;

/**
 * Kontrol listesi Madde 28 ve 29 geregi:
 * Gecersiz istekler (hatali parametre, gecersiz govde vb.) icin firlatilan ve 400 donduren custom exception.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}

