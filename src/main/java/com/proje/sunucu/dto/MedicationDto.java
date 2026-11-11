package com.proje.sunucu.dto;

/**
 * proje.md ve baslangic.md geregi:
 * Entity'leri dogrudan istemciye acmamak icin kullanilan veri transfer nesnesi (DTO).
 * Lombok veya baska kutuphane kullanilmadan sade Java sinifi olarak yazilmistir.
 */
public class MedicationDto {

    private Long id;
    private String name;
    private String activeIngredient;
    private String dosage;
    private String description;

    public MedicationDto() {
    }

    public MedicationDto(Long id, String name, String activeIngredient, String dosage, String description) {
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

