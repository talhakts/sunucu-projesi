package com.proje.sunucu.service;

import com.proje.sunucu.dto.ObservationNoteDto;
import com.proje.sunucu.dto.ObservationNoteRequestDto;

import java.util.List;

/**
 * proje.md ve kontrol listesi Madde 5 geregi:
 * Gozlem notu is mantigi ve kayit sahipligi denetimi arayuzu.
 */
public interface ObservationNoteService {

    List<ObservationNoteDto> getAllNotes();

    ObservationNoteDto getNoteById(Long id);

    List<ObservationNoteDto> getNotesByDoctorId(Long doctorId);

    List<ObservationNoteDto> getNotesByMedicationId(Long medicationId);

    ObservationNoteDto createNote(ObservationNoteRequestDto requestDto, String username);

    ObservationNoteDto updateNote(Long id, ObservationNoteRequestDto requestDto, String username, boolean isAdmin);

    void deleteNote(Long id, String username, boolean isAdmin);
}

