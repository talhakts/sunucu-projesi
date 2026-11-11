package com.proje.sunucu.repository;

import com.proje.sunucu.entity.Medication;

import java.util.List;
import java.util.Optional;

/**
 * Kontrol listesi Madde 6, 18 ve 19 geregi:
 * Repository arayuzu (Spring Data kullanilmaz).
 * CRUD ve parametreli JPQL filtreleme/sayfalama metotlari tanimlanmistir.
 */
public interface MedicationRepository {

    List<Medication> findAll();

    Optional<Medication> findById(Long id);

    Medication save(Medication medication);

    Medication update(Medication medication);

    void delete(Medication medication);

    List<Medication> findByFilter(String name, int page, int size, String sortBy, String sortDirection);

    long countByFilter(String name);
}
