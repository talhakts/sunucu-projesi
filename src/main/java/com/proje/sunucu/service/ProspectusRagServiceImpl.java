package com.proje.sunucu.service;

import com.proje.sunucu.dto.RagAnswerResponse;
import com.proje.sunucu.dto.RagQuestionRequest;
import com.proje.sunucu.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * proje.md geregi:
 * Spring AI / RAG prensiplerine uygun, ilac prospektus metinlerini parcalara (chunks) bolen,
 * kullanici sorusuna gore benzerlik aramasi yaparak ilgili metin parcasini bulan ve
 * kaynak destekli (grounded) yanit ureten servis implementasyonu.
 */
@Service
public class ProspectusRagServiceImpl implements ProspectusRagService {

    // Ornek ilac prospektus parcalari (Chunking deposu)
    private final Map<String, List<String>> prospectusChunks = new HashMap<>();

    public ProspectusRagServiceImpl() {
        // Parol Prospektus Parcalari
        List<String> parolChunks = new ArrayList<>();
        parolChunks.add("PAROL KULLANIM DOZU: Yetiskinlerde ve 12 yasin uzerindeki cocuklarda onerilen doz 4-6 saatte bir 500 mg - 1000 mg arasindadir. 24 saatte maksimum doz 4000 mg'i asmamalidir.");
        parolChunks.add("PAROL YAN ETKILERI: Yaygin gorulen yan etkiler bas donmesi ve uyku halidir. Seyrek olarak alerjik cilt dokuntuleri, trombositopeni ve yuksek dozda karaciger toksisitesi gorulebilir.");
        parolChunks.add("PAROL ETKEN MADDE VE ENDIKASYON: Etken maddesi parasetamoldur. Hafif ve orta siddetli agrilar ile atesin semptomatik tedavisinde kullanilir.");
        prospectusChunks.put("parol", parolChunks);

        // Aspirin Prospektus Parcalari
        List<String> aspirinChunks = new ArrayList<>();
        aspirinChunks.add("ASPIRIN KULLANIM DOZU: Agri ve ates durumlarinda 4-8 saatte bir 500 mg alinabilir. Gunluk maksimum doz 3000 mg'dir. Kardiyovaskuler korumada gunde 100 mg onerilir. Yemeklerden sonra bol suyla alinmalidir.");
        aspirinChunks.add("ASPIRIN YAN ETKILERI: Mide yanmasi, mide bulantisi, gastrointestinal kanama riski, kulak cinlamasi ve kanama suresinde uzama baslica yan etkileridir.");
        aspirinChunks.add("ASPIRIN ETKEN MADDE VE UYARILAR: Etken maddesi asetilsalisilik asittir. Mide ulseri olanlarda ve kanama bozuklugu bulunanlarda kontrendikedir. Cocuklarda viral enfeksiyon sirasinda Reye sendromu riski nedeniyle kullanilmamalidir.");
        prospectusChunks.put("aspirin", aspirinChunks);
    }

    @Override
    public RagAnswerResponse answerQuestion(RagQuestionRequest request) {
        if (request == null || request.getQuestion() == null || request.getQuestion().trim().isEmpty()) {
            throw new BadRequestException("Soru metni bos birakilamaz.");
        }

        String medKey = (request.getMedicationName() != null) ? request.getMedicationName().trim().toLowerCase() : "";
        List<String> chunks = prospectusChunks.get(medKey);

        if (chunks == null) {
            // Depodaki tum parcalarda arama yap
            chunks = new ArrayList<>();
            for (List<String> list : prospectusChunks.values()) {
                chunks.addAll(list);
            }
        }

        // Benzerlik aramasi (Similarity search / retrieval simulasyonu)
        String query = request.getQuestion().toLowerCase();
        String bestMatchingChunk = findBestChunk(query, chunks);

        String answer;
        String source;

        if (bestMatchingChunk != null) {
            source = "Sistem Prospektus Veritabanı (" + (medKey.isEmpty() ? "Genel" : medKey.toUpperCase()) + ")";
            answer = "Prospektus belgesine dayanan bilgi: " + bestMatchingChunk +
                     " [Bilgilendirme amaclidir; ilac kullanmadan once daima doktorunuza danisiniz.]";
        } else {
            source = "Dogrulanamayan Kaynak";
            answer = "Yuklenen prospektus metinlerinde sorunuzla eslesen kesin bir bilgi bulunamadi. Veri guvenligi ve hatali uretim (halusinasyon) riskini onlemek adina tahmini bilgi verilmemektedir.";
            bestMatchingChunk = "Eslesen parca bulunamadi.";
        }

        return new RagAnswerResponse(
                request.getMedicationName(),
                request.getQuestion(),
                answer,
                bestMatchingChunk,
                source
        );
    }

    private String findBestChunk(String query, List<String> chunks) {
        String bestChunk = null;
        int maxScore = 0;

        for (String chunk : chunks) {
            int score = calculateMatchScore(query, chunk.toLowerCase());
            if (score > maxScore) {
                maxScore = score;
                bestChunk = chunk;
            }
        }

        return maxScore > 0 ? bestChunk : null;
    }

    private int calculateMatchScore(String query, String chunkText) {
        int score = 0;
        String[] keywords = query.split("\\s+");
        for (String word : keywords) {
            if (word.length() > 2 && chunkText.contains(word)) {
                score += 2;
            }
        }
        if (query.contains("yan etki") && chunkText.contains("yan etki")) score += 5;
        if (query.contains("doz") && chunkText.contains("doz")) score += 5;
        if (query.contains("nedir") && chunkText.contains("endikasyon")) score += 3;
        return score;
    }
}

