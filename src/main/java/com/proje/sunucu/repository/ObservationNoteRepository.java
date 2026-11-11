package com.proje.sunucu.repository;

import com.proje.sunucu.entity.ObservationNote;

import java.util.List;
import java.util.Optional;

/**
 * proje.md ve kontrol listesi Madde 6, 18 geregi:
 * Gozlem notu veri erisim arayuzu (Spring Data arayuzleri kullanilmaz).
 */
public interface ObservationNoteRepository {

    List<ObservationNote> findAll();

    Optional<ObservationNote> findById(Long id);

    List<ObservationNote> findByDoctorId(Long doctorId);

    List<ObservationNote> findByMedicationId(Long medicationId);

    ObservationNote save(ObservationNote note);

    ObservationNote update(ObservationNote note);

    void delete(ObservationNote note);
}

