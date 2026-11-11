package com.proje.sunucu.dto;

/**
 * proje.md geregi:
 * Yapay zeka RAG soru-cevap istek modeli.
 */
public class RagQuestionRequest {

    private String medicationName;
    private String question;

    public RagQuestionRequest() {
    }

    public RagQuestionRequest(String medicationName, String question) {
        this.medicationName = medicationName;
        this.question = question;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}

