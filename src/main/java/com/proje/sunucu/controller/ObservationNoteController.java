package com.proje.sunucu.controller;

import com.proje.sunucu.dto.ObservationNoteDto;
import com.proje.sunucu.dto.ObservationNoteRequestDto;
import com.proje.sunucu.service.ObservationNoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Kontrol listesi Madde 35 ve proje.md geregi:
 * Gozlem notlari REST Controller sinifi ve OpenAPI dokumantasyonu.
 */
@RestController
@RequestMapping("/api/notes")
@Tag(name = "Gözlem Notu Yönetimi (ObservationNote)", description = "Hasta gözlem notu ekleme, okuma, güncelleme, silme ve kayıt sahipliği işlemleri")
public class ObservationNoteController {

    private final ObservationNoteService observationNoteService;

    public ObservationNoteController(ObservationNoteService observationNoteService) {
        this.observationNoteService = observationNoteService;
    }

    @Operation(summary = "Tüm gözlem notlarını listele", description = "Sistemdeki tüm hasta gözlem notlarını ilişkili doktor ve ilaç bilgileriyle döndürür.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Başarılı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping
    public ResponseEntity<List<ObservationNoteDto>> getAllNotes() {
        List<ObservationNoteDto> notes = observationNoteService.getAllNotes();
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Kimliğe göre gözlem notu getir", description = "Belirtilen ID'ye sahip gözlem notunun detaylarını döndürür.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Not bulundu"),
        @ApiResponse(responseCode = "404", description = "Gözlem notu bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ObservationNoteDto> getNoteById(@PathVariable("id") Long id) {
        ObservationNoteDto note = observationNoteService.getNoteById(id);
        return ResponseEntity.ok(note);
    }

    @Operation(summary = "Doktora göre gözlem notlarını listele", description = "Belirli bir doktorun oluşturduğu gözlem notlarını döndürür.")
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<ObservationNoteDto>> getNotesByDoctor(@PathVariable("doctorId") Long doctorId) {
        List<ObservationNoteDto> notes = observationNoteService.getNotesByDoctorId(doctorId);
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "İlaca ait gözlem notlarını listele", description = "Belirli bir ilaca iliştirilmiş hasta gözlem notlarını döndürür.")
    @GetMapping("/medication/{medicationId}")
    public ResponseEntity<List<ObservationNoteDto>> getNotesByMedication(@PathVariable("medicationId") Long medicationId) {
        List<ObservationNoteDto> notes = observationNoteService.getNotesByMedicationId(medicationId);
        return ResponseEntity.ok(notes);
    }

    @Operation(summary = "Yeni gözlem notu oluştur", description = "Giriş yapmış olan doktor adına yeni bir gözlem notu kaydeder.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Gözlem notu oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz içerik veya ilaç kimliği"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @PostMapping
    public ResponseEntity<ObservationNoteDto> createNote(
            @RequestBody ObservationNoteRequestDto requestDto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "doktor1";
        ObservationNoteDto created = observationNoteService.createNote(requestDto, username);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Gözlem notunu güncelle", description = "Gözlem notunu günceller. Kayıt sahipliği kuralı: Yalnızca notu oluşturan doktor veya ADMIN güncelleyebilir.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Not güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz veri veya yetkisiz kayıt sahipliği ihlali"),
        @ApiResponse(responseCode = "404", description = "Not bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ObservationNoteDto> updateNote(
            @PathVariable("id") Long id,
            @RequestBody ObservationNoteRequestDto requestDto,
            Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "doktor1";
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        ObservationNoteDto updated = observationNoteService.updateNote(id, requestDto, username, isAdmin);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Gözlem notunu sil", description = "Gözlem notunu siler. Kayıt sahipliği kuralı: Yalnızca notu oluşturan doktor veya ADMIN silebilir.")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Not silindi"),
        @ApiResponse(responseCode = "400", description = "Yetkisiz kayıt sahipliği ihlali"),
        @ApiResponse(responseCode = "404", description = "Not bulunamadı"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable("id") Long id, Authentication authentication) {
        String username = authentication != null ? authentication.getName() : "doktor1";
        boolean isAdmin = authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        observationNoteService.deleteNote(id, username, isAdmin);
        return ResponseEntity.noContent().build();
    }
}
