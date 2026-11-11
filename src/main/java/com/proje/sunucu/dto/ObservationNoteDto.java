package com.proje.sunucu.dto;

import java.time.LocalDateTime;

/**
 * proje.md ve baslangic.md geregi:
 * Gozlem notu verilerini istemciye donduren DTO.
 */
public class ObservationNoteDto {

    private Long id;
    private String noteContent;
    private LocalDateTime createdAt;
    private Long doctorId;
    private String doctorUsername;
    private String doctorFullName;
    private Long medicationId;
    private String medicationName;

    public ObservationNoteDto() {
    }

    public ObservationNoteDto(Long id, String noteContent, LocalDateTime createdAt,
                              Long doctorId, String doctorUsername, String doctorFullName,
                              Long medicationId, String medicationName) {
        this.id = id;
        this.noteContent = noteContent;
        this.createdAt = createdAt;
        this.doctorId = doctorId;
        this.doctorUsername = doctorUsername;
        this.doctorFullName = doctorFullName;
        this.medicationId = medicationId;
        this.medicationName = medicationName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorUsername() {
        return doctorUsername;
    }

    public void setDoctorUsername(String doctorUsername) {
        this.doctorUsername = doctorUsername;
    }

    public String getDoctorFullName() {
        return doctorFullName;
    }

    public void setDoctorFullName(String doctorFullName) {
        this.doctorFullName = doctorFullName;
    }

    public Long getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Long medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }
}
