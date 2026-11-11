package com.proje.sunucu.dto;

/**
 * proje.md geregi:
 * Yapay zeka RAG kaynak destekli cevap modeli.
 */
public class RagAnswerResponse {

    private String medicationName;
    private String question;
    private String answer;
    private String retrievedContextChunk;
    private String source;

    public RagAnswerResponse() {
    }

    public RagAnswerResponse(String medicationName, String question, String answer, String retrievedContextChunk, String source) {
        this.medicationName = medicationName;
        this.question = question;
        this.answer = answer;
        this.retrievedContextChunk = retrievedContextChunk;
        this.source = source;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getRetrievedContextChunk() {
        return retrievedContextChunk;
    }

    public void setRetrievedContextChunk(String retrievedContextChunk) {
        this.retrievedContextChunk = retrievedContextChunk;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}

