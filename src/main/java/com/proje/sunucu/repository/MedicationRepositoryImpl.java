package com.proje.sunucu.repository;

import com.proje.sunucu.entity.Medication;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Kontrol listesi Madde 6, 7, 18 ve 19 geregi:
 * JPA/Hibernate ve EntityManager uzerinden veri erisimi.
 * Parametreli JPQL sorgulari setParameter ile guvenli sekilde baglanmistir.
 */
@Repository
public class MedicationRepositoryImpl implements MedicationRepository {

    private final EntityManager entityManager;

    public MedicationRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Medication> findAll() {
        return entityManager.createQuery("SELECT m FROM Medication m", Medication.class)
                .getResultList();
    }

    @Override
    public Optional<Medication> findById(Long id) {
        Medication medication = entityManager.find(Medication.class, id);
        return Optional.ofNullable(medication);
    }

    @Override
    public Medication save(Medication medication) {
        entityManager.persist(medication);
        return medication;
    }

    @Override
    public Medication update(Medication medication) {
        return entityManager.merge(medication);
    }

    @Override
    public void delete(Medication medication) {
        entityManager.remove(medication);
    }

    // Madde 19: Parametreli JPQL filtre sorgusu, sayfalama ve siralama
    @Override
    public List<Medication> findByFilter(String name, int page, int size, String sortBy, String sortDirection) {
        // Siralama alanlari icin guvenli beyaz liste (whitelist) kontrolu
        String safeSortBy = "id";
        if ("name".equalsIgnoreCase(sortBy) || "activeIngredient".equalsIgnoreCase(sortBy) || "dosage".equalsIgnoreCase(sortBy)) {
            safeSortBy = sortBy;
        }
        String safeDirection = "DESC".equalsIgnoreCase(sortDirection) ? "DESC" : "ASC";

        String jpql = "SELECT m FROM Medication m WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(:name)) ORDER BY m." + safeSortBy + " " + safeDirection;

        TypedQuery<Medication> query = entityManager.createQuery(jpql, Medication.class);

        // Sorgu parametreleri setParameter ile baglanir
        String nameParam = (name != null && !name.trim().isEmpty()) ? "%" + name.trim() + "%" : null;
        query.setParameter("name", nameParam);

        // EntityManager sayfalama ayarlari
        query.setFirstResult(page * size);
        query.setMaxResults(size);

        return query.getResultList();
    }

    @Override
    public long countByFilter(String name) {
        String jpql = "SELECT COUNT(m) FROM Medication m WHERE (:name IS NULL OR LOWER(m.name) LIKE LOWER(:name))";
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);

        String nameParam = (name != null && !name.trim().isEmpty()) ? "%" + name.trim() + "%" : null;
        query.setParameter("name", nameParam);

        return query.getSingleResult();
    }
}
