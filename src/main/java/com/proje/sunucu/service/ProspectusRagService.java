package com.proje.sunucu.service;

import com.proje.sunucu.dto.RagAnswerResponse;
import com.proje.sunucu.dto.RagQuestionRequest;

/**
 * proje.md geregi:
 * Ilac prospektus metinleri uzerinden RAG akisi yuruten servis arayuzu.
 */
public interface ProspectusRagService {

    RagAnswerResponse answerQuestion(RagQuestionRequest request);
}

