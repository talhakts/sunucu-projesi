package com.proje.sunucu.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Kontrol listesi Madde 16 ve 17 geregi:
 * @Entity, @Table, @Column ve @Id acikca kullanilmistir.
 * Parametresiz public constructor bulunmaktadir.
 */
@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active_ingredient")
    private String activeIngredient;

    @Column(name = "dosage")
    private String dosage;

    @Column(name = "description", length = 1000)
    private String description;

    // JPA icin zorunlu parametresiz constructor
    public Medication() {
    }

    public Medication(Long id, String name, String activeIngredient, String dosage, String description) {
        this.id = id;
        this.name = name;
        this.activeIngredient = activeIngredient;
        this.dosage = dosage;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveIngredient() {
        return activeIngredient;
    }

    public void setActiveIngredient(String activeIngredient) {
        this.activeIngredient = activeIngredient;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

