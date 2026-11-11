package com.proje.sunucu.controller;

import com.proje.sunucu.dto.MedicationDto;
import com.proje.sunucu.dto.PageResponse;
import com.proje.sunucu.service.MedicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontrol listesi Madde 3, 4, 7, 19, 21-26 ve 35 geregi:
 * Ilac REST Controller sinifi ve OpenAPI dokumantasyonu.
 */
@RestController
@RequestMapping("/api/medications")
@Tag(name = "İlaç Yönetimi (Medication)", description = "İlaç ekleme, okuma, tam/kısmi güncelleme, silme ve filtreli sayfalama işlemleri")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @Operation(summary = "Tüm ilaçları listele", description = "Sistemde kayıtlı olan tüm ilaçları dizi olarak döndürür.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Başarılı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping
    public ResponseEntity<List<MedicationDto>> getAllMedications() {
        List<MedicationDto> medications = medicationService.getAllMedications();
        return ResponseEntity.ok(medications);
    }

    @Operation(summary = "İlaç arama, sayfalama ve sıralama", description = "İlaç adına göre parametreli JPQL filtreleme yapar, sayfalama ve sıralama uygular.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Başarılı sayfalama sonucu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz sayfalama parametreleri"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping("/search")
    public ResponseEntity<PageResponse<MedicationDto>> searchMedications(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "sortDirection", defaultValue = "ASC") String sortDirection) {
        PageResponse<MedicationDto> response = medicationService.searchMedications(name, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Kimliğe göre ilaç getir", description = "Belirtilen ID'ye sahip ilacın detaylarını döndürür.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "İlaç bulundu"),
        @ApiResponse(responseCode = "404", description = "Belirtilen ID ile ilaç bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MedicationDto> getMedicationById(@PathVariable("id") Long id) {
        MedicationDto medication = medicationService.getMedicationById(id);
        return ResponseEntity.ok(medication);
    }

    @Operation(summary = "Yeni ilaç oluştur", description = "Gönderilen JSON gövdesinden yeni bir ilaç kaydı oluşturur (Yalnızca ROLE_ADMIN).")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "İlaç başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz istek gövdesi veya eksik alan"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim (Yalnızca Admin ekleyebilir)")
    })
    @PostMapping
    public ResponseEntity<MedicationDto> createMedication(@RequestBody MedicationDto medicationDto) {
        MedicationDto created = medicationService.createMedication(medicationDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "İlacı tam güncelle (PUT)", description = "Belirtilen ilacın tüm alanlarını tam olarak günceller (Yalnızca ROLE_ADMIN).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "İlaç güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz veri"),
        @ApiResponse(responseCode = "404", description = "İlaç bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim")
    })
    @PutMapping("/{id}")
    public ResponseEntity<MedicationDto> updateMedication(
            @PathVariable("id") Long id,
            @RequestBody MedicationDto medicationDto) {
        MedicationDto updated = medicationService.updateMedication(id, medicationDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "İlacı kısmi güncelle (PATCH)", description = "Yalnızca gönderilen alanları günceller, gönderilmeyenleri korur; ID değiştirilemez (Yalnızca ROLE_ADMIN).")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "İlaç kısmi olarak güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz istek veya ID değiştirme teşebbüsü"),
        @ApiResponse(responseCode = "404", description = "İlaç bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim (PATCH güncelleme yetkisi ister)")
    })
    @PatchMapping("/{id}")
    public ResponseEntity<MedicationDto> patchMedication(
            @PathVariable("id") Long id,
            @RequestBody MedicationDto patchDto) {
        MedicationDto patched = medicationService.patchMedication(id, patchDto);
        return ResponseEntity.ok(patched);
    }

    @Operation(summary = "İlacı sil (DELETE)", description = "Belirtilen ID'ye sahip ilacı siler (Yalnızca ROLE_ADMIN).")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "İlaç başarıyla silindi"),
        @ApiResponse(responseCode = "404", description = "İlaç bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim (Yalnızca Admin silebilir)")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable("id") Long id) {
        medicationService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
}
