package com.proje.sunucu.controller;

import com.proje.sunucu.dto.RagAnswerResponse;
import com.proje.sunucu.dto.RagQuestionRequest;
import com.proje.sunucu.service.ProspectusRagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * proje.md geregi:
 * Yapay zeka ve RAG uzerinden prospektus soru-cevap REST Controller sinifi.
 */
@RestController
@RequestMapping("/api/ai")
@Tag(name = "Yapay Zekâ Soru-Cevap (RAG)", description = "İlaç prospektüsleri üzerinden parça benzerliği (chunking & retrieval) ile kaynak destekli yanıt üretimi")
public class RagController {

    private final ProspectusRagService prospectusRagService;

    public RagController(ProspectusRagService prospectusRagService) {
        this.prospectusRagService = prospectusRagService;
    }

    @Operation(summary = "İlaç prospektüsü üzerinden soru sor",
               description = "İlaç adı ve soru iletilir. Sistem prospektüs parçalarını tarayarak benzerlik aramasıyla kaynak destekli yanıt üretir.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Başarılı soru-cevap sonucu"),
        @ApiResponse(responseCode = "400", description = "Soru metni boş bırakılamaz"),
        @ApiResponse(responseCode = "401", description = "Kimlik doğrulaması başarısız")
    })
    @PostMapping("/ask")
    public ResponseEntity<RagAnswerResponse> askQuestion(@RequestBody RagQuestionRequest request) {
        RagAnswerResponse response = prospectusRagService.answerQuestion(request);
        return ResponseEntity.ok(response);
    }
}

