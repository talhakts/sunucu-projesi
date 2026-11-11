package com.proje.sunucu.service;

import com.proje.sunucu.dto.ObservationNoteDto;
import com.proje.sunucu.dto.ObservationNoteRequestDto;
import com.proje.sunucu.entity.Medication;
import com.proje.sunucu.entity.ObservationNote;
import com.proje.sunucu.entity.User;
import com.proje.sunucu.exception.BadRequestException;
import com.proje.sunucu.exception.ResourceNotFoundException;
import com.proje.sunucu.repository.MedicationRepository;
import com.proje.sunucu.repository.ObservationNoteRepository;
import com.proje.sunucu.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * proje.md ve kontrol listesi Madde 5, 7, 20 geregi:
 * Gozlem notu servis implementasyonu.
 * Bagimliliklar yalnizca constructor ile alinmistir.
 * Kayit sahipligi denetimi (doktor yalnizca kendi olusturdugu notu duzenleyebilir/silebilir) saglanmistir.
 */
@Service
public class ObservationNoteServiceImpl implements ObservationNoteService {

    private final ObservationNoteRepository observationNoteRepository;
    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;

    public ObservationNoteServiceImpl(ObservationNoteRepository observationNoteRepository,
                                      UserRepository userRepository,
                                      MedicationRepository medicationRepository) {
        this.observationNoteRepository = observationNoteRepository;
        this.userRepository = userRepository;
        this.medicationRepository = medicationRepository;
    }

    @Override
    public List<ObservationNoteDto> getAllNotes() {
        List<ObservationNote> notes = observationNoteRepository.findAll();
        List<ObservationNoteDto> dtos = new ArrayList<>();
        for (ObservationNote note : notes) {
            dtos.add(convertToDto(note));
        }
        return dtos;
    }

    @Override
    public ObservationNoteDto getNoteById(Long id) {
        ObservationNote note = observationNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Gozlem notu bulunamadi: " + id));
        return convertToDto(note);
    }

    @Override
    public List<ObservationNoteDto> getNotesByDoctorId(Long doctorId) {
        List<ObservationNote> notes = observationNoteRepository.findByDoctorId(doctorId);
        List<ObservationNoteDto> dtos = new ArrayList<>();
        for (ObservationNote note : notes) {
            dtos.add(convertToDto(note));
        }
        return dtos;
    }

    @Override
    public List<ObservationNoteDto> getNotesByMedicationId(Long medicationId) {
        List<ObservationNote> notes = observationNoteRepository.findByMedicationId(medicationId);
        List<ObservationNoteDto> dtos = new ArrayList<>();
        for (ObservationNote note : notes) {
            dtos.add(convertToDto(note));
        }
        return dtos;
    }

    @Override
    @Transactional
    public ObservationNoteDto createNote(ObservationNoteRequestDto requestDto, String username) {
        if (requestDto.getNoteContent() == null || requestDto.getNoteContent().trim().isEmpty()) {
            throw new BadRequestException("Gozlem notu icerigi bos birakilamaz.");
        }
        if (requestDto.getMedicationId() == null) {
            throw new BadRequestException("Gozlem notunun iliskilendirilecegi ilac kimligi (medicationId) zorunludur.");
        }

        User doctor = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Notu olusturacak kullanici bulunamadi: " + username));

        Medication medication = medicationRepository.findById(requestDto.getMedicationId())
                .orElseThrow(() -> new ResourceNotFoundException("Iliskili ilac bulunamadi: " + requestDto.getMedicationId()));

        ObservationNote note = new ObservationNote(
                null,
                requestDto.getNoteContent().trim(),
                LocalDateTime.now(),
                doctor,
                medication
        );

        ObservationNote saved = observationNoteRepository.save(note);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public ObservationNoteDto updateNote(Long id, ObservationNoteRequestDto requestDto, String username, boolean isAdmin) {
        ObservationNote existing = observationNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guncellenecek gozlem notu bulunamadi: " + id));

        // Kayit sahipligi denetimi (proje.md kurali)
        if (!isAdmin && !existing.getDoctor().getUsername().equalsIgnoreCase(username)) {
            throw new BadRequestException("Bu notu yalnizca olusturan doktor (" + existing.getDoctor().getUsername() + ") veya sistem yoneticisi guncelleyebilir.");
        }

        if (requestDto.getNoteContent() == null || requestDto.getNoteContent().trim().isEmpty()) {
            throw new BadRequestException("Gozlem notu icerigi bos birakilamaz.");
        }

        existing.setNoteContent(requestDto.getNoteContent().trim());

        if (requestDto.getMedicationId() != null && !requestDto.getMedicationId().equals(existing.getMedication().getId())) {
            Medication medication = medicationRepository.findById(requestDto.getMedicationId())
                    .orElseThrow(() -> new ResourceNotFoundException("Iliskili ilac bulunamadi: " + requestDto.getMedicationId()));
            existing.setMedication(medication);
        }

        ObservationNote updated = observationNoteRepository.update(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteNote(Long id, String username, boolean isAdmin) {
        ObservationNote existing = observationNoteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Silinecek gozlem notu bulunamadi: " + id));

        // Kayit sahipligi denetimi (proje.md kurali)
        if (!isAdmin && !existing.getDoctor().getUsername().equalsIgnoreCase(username)) {
            throw new BadRequestException("Bu notu yalnizca olusturan doktor (" + existing.getDoctor().getUsername() + ") veya sistem yoneticisi silebilir.");
        }

        observationNoteRepository.delete(existing);
    }

    private ObservationNoteDto convertToDto(ObservationNote note) {
        return new ObservationNoteDto(
                note.getId(),
                note.getNoteContent(),
                note.getCreatedAt(),
                note.getDoctor().getId(),
                note.getDoctor().getUsername(),
                note.getDoctor().getFullName(),
                note.getMedication().getId(),
                note.getMedication().getName()
        );
    }
}

