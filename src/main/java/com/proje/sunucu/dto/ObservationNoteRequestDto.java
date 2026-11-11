package com.proje.sunucu.dto;

/**
 * proje.md ve baslangic.md geregi:
 * Gozlem notu ekleme ve guncelleme isteklerinde kullanilan veri transfer nesnesi (DTO).
 */
public class ObservationNoteRequestDto {

    private String noteContent;
    private Long medicationId;

    public ObservationNoteRequestDto() {
    }

    public ObservationNoteRequestDto(String noteContent, Long medicationId) {
        this.noteContent = noteContent;
        this.medicationId = medicationId;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }
}

