package com.proje.sunucu.service;

import com.proje.sunucu.dto.MedicationDto;
import com.proje.sunucu.dto.PageResponse;
import com.proje.sunucu.entity.Medication;
import com.proje.sunucu.exception.BadRequestException;
import com.proje.sunucu.exception.ResourceNotFoundException;
import com.proje.sunucu.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Kontrol listesi Madde 5, 7, 19, 20 ve 22-25 geregi:
 * Service katmani implementasyonu.
 */
@Service
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;

    public MedicationServiceImpl(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @Override
    public List<MedicationDto> getAllMedications() {
        List<Medication> medications = medicationRepository.findAll();
        List<MedicationDto> dtos = new ArrayList<>();
        for (Medication medication : medications) {
            dtos.add(convertToDto(medication));
        }
        return dtos;
    }

    @Override
    public MedicationDto getMedicationById(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Belirtilen ID ile ilac bulunamadi: " + id));
        return convertToDto(medication);
    }

    @Override
    @Transactional
    public MedicationDto createMedication(MedicationDto medicationDto) {
        if (medicationDto.getName() == null || medicationDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Ilac adi bos birakilamaz.");
        }

        Medication medication = new Medication(
                null,
                medicationDto.getName().trim(),
                medicationDto.getActiveIngredient(),
                medicationDto.getDosage(),
                medicationDto.getDescription()
        );

        Medication saved = medicationRepository.save(medication);
        return convertToDto(saved);
    }

    @Override
    @Transactional
    public MedicationDto updateMedication(Long id, MedicationDto medicationDto) {
        Medication existing = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guncellenecek ilac bulunamadi: " + id));

        if (medicationDto.getName() == null || medicationDto.getName().trim().isEmpty()) {
            throw new BadRequestException("Ilac adi bos birakilamaz.");
        }

        existing.setName(medicationDto.getName().trim());
        existing.setActiveIngredient(medicationDto.getActiveIngredient());
        existing.setDosage(medicationDto.getDosage());
        existing.setDescription(medicationDto.getDescription());

        Medication updated = medicationRepository.update(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public MedicationDto patchMedication(Long id, MedicationDto patchDto) {
        Medication existing = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Guncellenecek ilac bulunamadi: " + id));

        if (patchDto.getId() != null && !patchDto.getId().equals(id)) {
            throw new BadRequestException("PATCH islemi ile ilac kimligi (ID) degistirilemez.");
        }

        if (patchDto.getName() != null) {
            if (patchDto.getName().trim().isEmpty()) {
                throw new BadRequestException("Ilac adi bos birakilamaz.");
            }
            existing.setName(patchDto.getName().trim());
        }

        if (patchDto.getActiveIngredient() != null) {
            existing.setActiveIngredient(patchDto.getActiveIngredient());
        }

        if (patchDto.getDosage() != null) {
            existing.setDosage(patchDto.getDosage());
        }

        if (patchDto.getDescription() != null) {
            existing.setDescription(patchDto.getDescription());
        }

        Medication updated = medicationRepository.update(existing);
        return convertToDto(updated);
    }

    @Override
    @Transactional
    public void deleteMedication(Long id) {
        Medication existing = medicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Silinecek ilac bulunamadi: " + id));

        medicationRepository.delete(existing);
    }

    // Madde 19 & proje.md: Parametreli JPQL filtre sorgusu, sayfalama ve siralama
    @Override
    public PageResponse<MedicationDto> searchMedications(String name, int page, int size, String sortBy, String sortDirection) {
        if (page < 0) {
            throw new BadRequestException("Sayfa numarasi 0'dan kucuk olamaz.");
        }
        if (size <= 0) {
            throw new BadRequestException("Sayfa boyutu 0'dan buyuk olmalidir.");
        }

        List<Medication> medications = medicationRepository.findByFilter(name, page, size, sortBy, sortDirection);
        long totalElements = medicationRepository.countByFilter(name);
        int totalPages = totalElements == 0 ? 0 : (int) Math.ceil((double) totalElements / size);

        List<MedicationDto> dtos = new ArrayList<>();
        for (Medication medication : medications) {
            dtos.add(convertToDto(medication));
        }

        return new PageResponse<>(dtos, page, size, totalElements, totalPages);
    }

    private MedicationDto convertToDto(Medication medication) {
        return new MedicationDto(
                medication.getId(),
                medication.getName(),
                medication.getActiveIngredient(),
                medication.getDosage(),
                medication.getDescription()
        );
    }
}
