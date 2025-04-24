package com.saglikAdimiAPI.Service;

import java.time.LocalDate;
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
import com.saglikAdimiAPI.Model.Doctor;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Model.Person;

@Service
public class SagliktaAIService implements SagliktaAIActionable {
	private String apiKey = "AIzaSyAxiGEjbM749zV_Umldw2eCAQSbHKWFNEY";

	private final RestTemplate restTemplate = new RestTemplate();
	private final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";

	private final ChatService chatService;
	private final ReadableUserService readableUserService;
	private final ReadablePatientService readablePatientService;
	private final ReadableDoctorService readableDoctorService;

	@Autowired
	public SagliktaAIService(ChatService chatService, ReadableUserService readableUserService,
			ReadablePatientService readablePatientService, ReadableDoctorService readableDoctorService) {
		this.chatService = chatService;
		this.readableUserService = readableUserService;
		this.readablePatientService = readablePatientService;
		this.readableDoctorService = readableDoctorService;
	}

	@Override
	public String askSagliktaAI(String message, String token) {
		try {

			// Kullanıcı bilgilerini al
			List<Chats> chatsList = chatService.getAllChat(token).getBody();
			Person person = readableUserService.getLoggedPerson(token).getBody();

			// Geçmiş sohbeti formatla
			String chatHistory = chatsList.stream().map(Chats::toString).collect(Collectors.joining("\n"));

			// Kullanıcının rolünü al
			String userRole = person.getRole();

			// Farklı roller için farklı prompt oluştur
			String prompt;
			if ("HASTA".equals(userRole)) {

				// Hasta bilgilerini al
				Patient patient = getPatient(person.getUserID(), token);
				String diseases = patient
						.getDiseases().stream().map(disease -> String.format("Hastalık: %s, Teşhis Tarihi: %s",
								disease.getDiseaseName(), disease.getDateOfDiagnosis()))
						.collect(Collectors.joining("\n"));

				// Eğer rol HASTA ise, hastaya uygun prompt
				prompt = """
						Kullanıcı bilgileri:
						- Ad: %s
						- Rol: %s
						- Hastalıklar ve Teşhis Tarihleri:
						%s

						Geçmiş sohbetler:
						%s

						Kullanıcının mesajı:
						%s

						Lütfen yukarıdaki bilgilere göre tıbbi olarak doğru ve empatik bir yanıt üret, hastaya yardımcı ol.
						"""
						.formatted(person.getName(), userRole, diseases, chatHistory, message);
			} else if ("DOKTOR".equals(userRole)) {

				// Doktor bilgilerini al
				Doctor doctor = getDoctor(person.getUserID(), token);
				String announcements = doctor.getAnnouncement().stream().map(announcement -> String
						.format("Başlık: %s, İçerik: %s", announcement.getTitle(), announcement.getContent()))
						.collect(Collectors.joining("\n"));

				String contactInfo = doctor.getContactInfor().stream()
						.map(info -> String.format("E-posta: %s, Telefon: %s", info.getEmail(), info.getPhoneNumber()))
						.collect(Collectors.joining("\n"));

				String specialization = doctor
						.getSpecialization().stream().map(spec -> String.format("Uzmanlık: %s, Deneyim: %d yıl",
								spec.getNameOfSpecialization(), spec.getSpecializationExperience()))
						.collect(Collectors.joining("\n"));

				String workAddress = doctor.getWorksAddress().stream()
						.map(address -> String.format("İşyeri: %s, Adres: %s, %s, %s, %s", address.getWorkPlaceName(),
								address.getStreet(), address.getCity(), address.getCounty(), address.getCountry()))
						.collect(Collectors.joining("\n"));

				// Eğer rol DOKTOR ise, doktora uygun prompt
				prompt = """
						Kullanıcı bilgileri:
						- Ad: %s
						- Rol: %s
						- Duyurular:
						%s
						- İletişim Bilgileri:
						%s
						- Uzmanlıklar:
						%s
						- Çalışma Adresleri:
						%s

						Geçmiş sohbetler:
						%s

						Kullanıcının mesajı:
						%s

						Lütfen yukarıdaki bilgilere göre tıbbi olarak doğru ve profesyonel bir yanıt ver, doktor açısından uygun bir bilgi ver.
						"""
						.formatted(person.getName(), userRole, announcements, contactInfo, specialization, workAddress,
								chatHistory, message);
			} else {
				// Diğer roller için genel bir prompt
				prompt = """
						Kullanıcı bilgileri:
						- Ad: %s
						- Rol: %s

						Geçmiş sohbetler:
						%s

						Kullanıcının mesajı:
						%s

						Lütfen yukarıdaki bilgilere göre uygun bir yanıt üret.
						""".formatted(person.getName(), userRole, chatHistory, message);
			}

			// API isteği gönderme
			String url = GEMINI_URL + apiKey;

			Map<String, Object> requestBody = Map.of("contents",
					List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
			ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

			if (response.getStatusCode().is2xxSuccessful()) {
				// API'den gelen yanıtı işleme
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

	public Patient getPatient(int userID, String token) {
		return readablePatientService.getPatient(userID, token).getBody();
	}

	public Doctor getDoctor(int userID, String token) {
		return readableDoctorService.getDoctor(userID, token).getBody();
	}
}

/*
 * @Override public String askSagliktaAI(String message, String token) { // TODO
 * Auto-generated method stub
 * 
 * ChatService chatService = new ChatService(null); ReadableUserService
 * readableUserService = new ReadableUserService(null); List<Chats> chatsList =
 * (List<Chats>) chatService.getAllChat(token); Person person =
 * readableUserService.getLoggedPerson(token).getBody();
 * 
 * String url = GEMINI_URL + apiKey;
 * 
 * // API body yapısı Map<String, Object> requestBody = Map.of("contents",
 * List.of(Map.of("parts", List.of(Map.of("text", message)))));
 * 
 * HttpHeaders headers = new HttpHeaders();
 * headers.setContentType(MediaType.APPLICATION_JSON);
 * 
 * HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody,
 * headers);
 * 
 * ResponseEntity<Map> response = restTemplate.postForEntity(url, request,
 * Map.class);
 * 
 * if (response.getStatusCode().is2xxSuccessful()) { Map candidate = (Map)
 * ((List) ((Map) response.getBody()).get("candidates")).get(0); Map content =
 * (Map) candidate.get("content"); List parts = (List) content.get("parts");
 * return (String) ((Map) parts.get(0)).get("text"); } else { return
 * "API çağrısı başarısız: " + response.getStatusCode(); } }
 */
