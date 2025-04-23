package com.saglikAdimiAPI.Service;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saglikAdimiAPI.Abstraction.SagliktaAIActionable;

@Service
public class SagliktaAIService implements SagliktaAIActionable{
	private String apiKey = "AIzaSyAxiGEjbM749zV_Umldw2eCAQSbHKWFNEY";

	private final RestTemplate restTemplate = new RestTemplate();
	private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

	public String getGeminiAnswer(String prompt) {
		String url = GEMINI_URL + apiKey;

		// API body yapısı
		Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			Map candidate = (Map) ((List) ((Map) response.getBody()).get("candidates")).get(0);
			Map content = (Map) candidate.get("content");
			List parts = (List) content.get("parts");
			return (String) ((Map) parts.get(0)).get("text");
		} else {
			return "API çağrısı başarısız: " + response.getStatusCode();
		}
	}

	@Override
	public String askSagliktaAI(String message, String token) {
		// TODO Auto-generated method stub
		String url = GEMINI_URL + apiKey;

		// API body yapısı
		Map<String, Object> requestBody = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", message)))));

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			Map candidate = (Map) ((List) ((Map) response.getBody()).get("candidates")).get(0);
			Map content = (Map) candidate.get("content");
			List parts = (List) content.get("parts");
			return (String) ((Map) parts.get(0)).get("text");
		} else {
			return "API çağrısı başarısız: " + response.getStatusCode();
		}
	}
}