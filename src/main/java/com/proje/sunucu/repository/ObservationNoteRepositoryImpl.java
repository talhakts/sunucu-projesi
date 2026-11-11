package com.proje.sunucu.repository;

import com.proje.sunucu.entity.ObservationNote;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Kontrol listesi Madde 6, 7 ve 18 geregi:
 * Gozlem notu veritabani erisimi JPA/Hibernate ve EntityManager ile gerceklestirilir.
 */
@Repository
public class ObservationNoteRepositoryImpl implements ObservationNoteRepository {

    private final EntityManager entityManager;

    public ObservationNoteRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ObservationNote> findAll() {
        return entityManager.createQuery(
                "SELECT n FROM ObservationNote n JOIN FETCH n.doctor JOIN FETCH n.medication ORDER BY n.createdAt DESC",
                ObservationNote.class)
                .getResultList();
    }

    @Override
    public Optional<ObservationNote> findById(Long id) {
        TypedQuery<ObservationNote> query = entityManager.createQuery(
                "SELECT n FROM ObservationNote n JOIN FETCH n.doctor JOIN FETCH n.medication WHERE n.id = :id",
                ObservationNote.class);
        query.setParameter("id", id);
        List<ObservationNote> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<ObservationNote> findByDoctorId(Long doctorId) {
        TypedQuery<ObservationNote> query = entityManager.createQuery(
                "SELECT n FROM ObservationNote n JOIN FETCH n.doctor JOIN FETCH n.medication WHERE n.doctor.id = :doctorId ORDER BY n.createdAt DESC",
                ObservationNote.class);
        query.setParameter("doctorId", doctorId);
        return query.getResultList();
    }

    @Override
    public List<ObservationNote> findByMedicationId(Long medicationId) {
        TypedQuery<ObservationNote> query = entityManager.createQuery(
                "SELECT n FROM ObservationNote n JOIN FETCH n.doctor JOIN FETCH n.medication WHERE n.medication.id = :medicationId ORDER BY n.createdAt DESC",
                ObservationNote.class);
        query.setParameter("medicationId", medicationId);
        return query.getResultList();
    }

    @Override
    public ObservationNote save(ObservationNote note) {
        entityManager.persist(note);
        return note;
    }

    @Override
    public ObservationNote update(ObservationNote note) {
        return entityManager.merge(note);
    }

    @Override
    public void delete(ObservationNote note) {
        entityManager.remove(note);
    }
}

