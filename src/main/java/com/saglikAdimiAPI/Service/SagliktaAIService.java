package com.saglikAdimiAPI.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.saglikAdimiAPI.Abstraction.SagliktaAIActionable;
import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Model.Person;

@Service
public class SagliktaAIService implements SagliktaAIActionable{
	private String apiKey = "AIzaSyAxiGEjbM749zV_Umldw2eCAQSbHKWFNEY";

	private final RestTemplate restTemplate = new RestTemplate();
	private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
	
	/*
	@Override
	public String askSagliktaAI(String message, String token) {
		// TODO Auto-generated method stub
		
		ChatService chatService = new ChatService(null);
		ReadableUserService readableUserService = new ReadableUserService(null);
		List<Chats> chatsList = (List<Chats>) chatService.getAllChat(token);
		Person person = readableUserService.getLoggedPerson(token).getBody();		
		
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
	*/
	
    private final ChatService chatService;
    private final ReadableUserService readableUserService;

    @Autowired
    public SagliktaAIService(ChatService chatService, ReadableUserService readableUserService) {
        this.chatService = chatService;
        this.readableUserService = readableUserService;
    }
	
	
	   @Override
	    public String askSagliktaAI(String message, String token) {
	        try {


	            List<Chats> chatsList = chatService.getAllChat(token).getBody();
	            Person person = readableUserService.getLoggedPerson(token).getBody();

	            String chatHistory = chatsList.stream()
	                    .map(Chats::toString)
	                    .collect(Collectors.joining("\n"));

	            // Prompt oluşturuluyor
	            String prompt = """
	                Kullanıcı bilgileri:
	                - Ad: %s
	                - Rol: %s	                

	                Geçmiş sohbetler:
	                %s

	                Kullanıcının mesajı:
	                %s

	                Lütfen yukarıdaki bilgilere göre tıbbi olarak doğru ve empatik bir yanıt üret.
	                """.formatted(
	                    person.getName(),
	                    person.getRole(),	                    
	                    chatHistory,
	                    message
	                );

	            String url = GEMINI_URL + apiKey;

	            Map<String, Object> requestBody = Map.of(
	                "contents", List.of(
	                    Map.of("parts", List.of(
	                        Map.of("text", prompt)
	                    ))
	                )
	            );

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

	        } catch (Exception e) {
	            return "AI cevabı alınırken bir hata oluştu: " + e.getMessage();
	        }
	    }
	
}


