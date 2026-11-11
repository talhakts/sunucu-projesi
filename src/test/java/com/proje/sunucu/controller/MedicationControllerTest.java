package com.proje.sunucu.controller;

import com.proje.sunucu.dto.MedicationDto;
import com.proje.sunucu.exception.BadRequestException;
import com.proje.sunucu.exception.ResourceNotFoundException;
import com.proje.sunucu.service.MedicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Spring Boot 4.1.2 ve MockMvc ile yazilmis Controller testleri.
 * Boot 4'te @MockBean yerine @MockitoBean, ve org.springframework.boot.webmvc.test.autoconfigure paketi kullanilmistir.
 * GET liste, GET tekil, POST, PUT, PATCH, DELETE ile 400, 401, 403, 404 durumlari test edilir.
 */
@SpringBootTest
@AutoConfigureMockMvc
class MedicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MedicationService medicationService;

    @Test
    @DisplayName("GET /api/medications - Yetkili kullanici ile ilac listesi (200 OK)")
    @WithMockUser(username = "doktor1", roles = {"DOCTOR"})
    void getAllMedications_ShouldReturnOk() throws Exception {
        MedicationDto dto = new MedicationDto(1L, "Parol", "Parasetamol", "500mg", "Agri kesici");
        Mockito.when(medicationService.getAllMedications()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/medications")
                        .with(user("doktor1").roles("DOCTOR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Parol"))
                .andExpect(jsonPath("$[0].activeIngredient").value("Parasetamol"));
    }

    @Test
    @DisplayName("GET /api/medications/1 - Yetkili kullanici ile tekil ilac (200 OK)")
    @WithMockUser(username = "doktor1", roles = {"DOCTOR"})
    void getMedicationById_ShouldReturnOk() throws Exception {
        MedicationDto dto = new MedicationDto(1L, "Parol", "Parasetamol", "500mg", "Agri kesici");
        Mockito.when(medicationService.getMedicationById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/medications/1")
                        .with(user("doktor1").roles("DOCTOR")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Parol"));
    }

    @Test
    @DisplayName("GET /api/medications/999 - Bulunamayan kayit icin 404 Not Found")
    @WithMockUser(username = "doktor1", roles = {"DOCTOR"})
    void getMedicationById_NotFound_ShouldReturn404() throws Exception {
        Mockito.when(medicationService.getMedicationById(999L))
                .thenThrow(new ResourceNotFoundException("Belirtilen ID ile ilac bulunamadi: 999"));

        mockMvc.perform(get("/api/medications/999")
                        .with(user("doktor1").roles("DOCTOR")))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("POST /api/medications - ADMIN rolunde ilac olusturma (201 Created)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createMedication_WithAdmin_ShouldReturnCreated() throws Exception {
        MedicationDto created = new MedicationDto(2L, "Aspirin", "Asetilsalisilik asit", "100mg", "Kan sulandirici");

        Mockito.when(medicationService.createMedication(any(MedicationDto.class))).thenReturn(created);

        String jsonBody = "{\"name\":\"Aspirin\",\"activeIngredient\":\"Asetilsalisilik asit\",\"dosage\":\"100mg\",\"description\":\"Kan sulandirici\"}";

        mockMvc.perform(post("/api/medications")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("Aspirin"));
    }

    @Test
    @DisplayName("POST /api/medications - Gecersiz govde ile 400 Bad Request")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createMedication_BadRequest_ShouldReturn400() throws Exception {
        Mockito.when(medicationService.createMedication(any(MedicationDto.class)))
                .thenThrow(new BadRequestException("Ilac adi bos birakilamaz."));

        String jsonBody = "{\"name\":\"\"}";

        mockMvc.perform(post("/api/medications")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    @DisplayName("POST /api/medications - DOCTOR rolunde ilac ekleme engellenmeli (403 Forbidden)")
    @WithMockUser(username = "doktor1", roles = {"DOCTOR"})
    void createMedication_WithDoctor_ShouldReturnForbidden() throws Exception {
        String jsonBody = "{\"name\":\"Aspirin\",\"activeIngredient\":\"Asetilsalisilik asit\",\"dosage\":\"100mg\",\"description\":\"Kan sulandirici\"}";

        mockMvc.perform(post("/api/medications")
                        .with(user("doktor1").roles("DOCTOR"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"));
    }

    @Test
    @DisplayName("PUT /api/medications/1 - ADMIN rolunde tam guncelleme (200 OK)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateMedication_WithAdmin_ShouldReturnOk() throws Exception {
        MedicationDto updated = new MedicationDto(1L, "Parol Plus", "Parasetamol + Kafein", "500mg", "Guclendirilmis agri kesici");
        Mockito.when(medicationService.updateMedication(eq(1L), any(MedicationDto.class))).thenReturn(updated);

        String jsonBody = "{\"name\":\"Parol Plus\",\"activeIngredient\":\"Parasetamol + Kafein\",\"dosage\":\"500mg\",\"description\":\"Guclendirilmis agri kesici\"}";

        mockMvc.perform(put("/api/medications/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Parol Plus"));
    }

    @Test
    @DisplayName("PATCH /api/medications/1 - ADMIN rolunde kismi guncelleme (200 OK)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void patchMedication_WithAdmin_ShouldReturnOk() throws Exception {
        MedicationDto patched = new MedicationDto(1L, "Parol", "Parasetamol", "1000mg", "Agri kesici");
        Mockito.when(medicationService.patchMedication(eq(1L), any(MedicationDto.class))).thenReturn(patched);

        String jsonBody = "{\"dosage\":\"1000mg\"}";

        mockMvc.perform(patch("/api/medications/1")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dosage").value("1000mg"));
    }

    @Test
    @DisplayName("DELETE /api/medications/1 - ADMIN rolunde silme (204 No Content)")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteMedication_WithAdmin_ShouldReturnNoContent() throws Exception {
        Mockito.doNothing().when(medicationService).deleteMedication(1L);

        mockMvc.perform(delete("/api/medications/1")
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("GET /api/medications - Kimlik dogrulamasi olmadan istek (401 Unauthorized)")
    void getMedications_WithoutAuth_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/medications"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }
}
