package com.proje.sunucu.service;

import com.proje.sunucu.dto.MedicationDto;
import com.proje.sunucu.dto.PageResponse;

import java.util.List;

/**
 * Kontrol listesi Madde 5 ve 19 geregi:
 * Is mantigi ve akislari bu katmanda yurutulmektedir.
 */
public interface MedicationService {

    List<MedicationDto> getAllMedications();

    MedicationDto getMedicationById(Long id);

    MedicationDto createMedication(MedicationDto medicationDto);

    MedicationDto updateMedication(Long id, MedicationDto medicationDto);

    MedicationDto patchMedication(Long id, MedicationDto patchDto);

    void deleteMedication(Long id);

    PageResponse<MedicationDto> searchMedications(String name, int page, int size, String sortBy, String sortDirection);
}
