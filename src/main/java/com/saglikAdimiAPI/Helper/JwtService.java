package com.saglikAdimiAPI.Helper;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.saglikAdimiAPI.Model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {

	private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

	// Geliştirme ortamı için sabit SECRET_KEY (üretim ortamında güvenli ve farklı
	// bir değer kullanın)
	private static final String SECRET_KEY = "my_super_secret_key_which_is_very_secure_123456";
	// Token geçerlilik süresi: 1 saat (3600000 ms)
	private static final long EXPIRATION_TIME = 3600000;
	// Refresh token geçerlilik süresi: 1 ay (30 gün = 2592000000 ms)
	private static final long REFRESH_TOKEN_EXPIRATION_TIME = 2592000000L;

	// Token oluşturma metodu
	public String generateToken(Person person) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			String token = JWT.create().withIssuer("SaglikAdimiAPI") // Token'ı oluşturan sistem
					.withSubject(person.getEmail()) // Token sahibi (ör. kullanıcı email'i)
					.withClaim("userID", person.getUserID()) // Kullanıcı ID'si
					.withClaim("name", person.getName()) // Kullanıcı adı
					.withClaim("surname", person.getSurname()) // Kullanıcı soyadı
					.withClaim("role", person.getRole()) // Kullanıcı rolü
					.withIssuedAt(new Date()) // Oluşturulma zamanı
					.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Geçerlilik süresi
					.sign(algorithm); // İmzalama
			logger.info("{}", token);
			return token;
		} catch (Exception e) {
			logger.error("Token oluşturulurken hata meydana geldi: {}", e.getMessage());
			throw new RuntimeException("Token oluşturulamadı", e);
		}
	}

	// Access token oluşturma metodu
	public String generateAccessToken(Person person) {
		try {

			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			String accessToken = JWT.create().withIssuer("SaglikAdimiAPI") // Token'ı oluşturan sistem
					.withSubject(person.getEmail()) // Token sahibi (ör. kullanıcı email'i)
					.withClaim("userID", person.getUserID()) // Kullanıcı ID'si
					.withClaim("name", person.getName()) // Kullanıcı adı
					.withClaim("surname", person.getSurname()) // Kullanıcı soyadı
					.withClaim("role", person.getRole()) // Kullanıcı rolü
					.withIssuedAt(new Date()) // Oluşturulma zamanı
					.withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME)) // Geçerlilik
																											// süresi
																											// (15 dk)
					.sign(algorithm); // İmzalama
			logger.info("Access Token oluşturuldu: {}", accessToken);
			return accessToken;
		} catch (Exception e) {
			logger.error("Access token oluşturulurken hata meydana geldi: {}", e.getMessage());
			throw new RuntimeException("Access Token oluşturulamadı", e);
		}
	}

	// Token doğrulama metodu
	public DecodedJWT validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
			DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer("SaglikAdimiAPI") // Issuer kontrolü
					.build().verify(token);
			logger.info("Token başarıyla doğrulandı.");
			return decodedJWT;
		} catch (Exception e) {
			logger.error("Token doğrulanamadı: {}", e.getMessage());
			// Eğer token üzerinde oynama, ekstra boşluk ya da karakter sorunu varsa bunu
			// loglarda görebilirsiniz.
			throw new RuntimeException("Geçersiz token: " + e.getMessage(), e);
		}
	}

	// Token'dan kullanıcı adı (email) alma metodu
	public String getUsernameFromToken(String token) {
		DecodedJWT decodedJWT = validateToken(token);
		return decodedJWT.getSubject();
	}

	// Token'dan kullanıcı bilgilerini alma metodu
	public Person getPersonFromToken(String token) {

		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}

		DecodedJWT decodedJWT = validateToken(token);
		int userID = decodedJWT.getClaim("userID").asInt();
		String name = decodedJWT.getClaim("name").asString();
		String surname = decodedJWT.getClaim("surname").asString();
		String role = decodedJWT.getClaim("role").asString();
		String email = decodedJWT.getSubject();
		return new Person(userID, name, surname, email, role);
	}

	// Token'ın süresinin dolup dolmadığını kontrol etme metodu
	public boolean isTokenExpired(String token) {
		// Eğer token "Bearer " ile başlıyorsa öneki kaldırıyoruz.
		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		}
		try {
			DecodedJWT decodedJWT = validateToken(token);
			boolean expired = decodedJWT.getExpiresAt().before(new Date());
			if (expired) {
				logger.info("Token süresi dolmuş.");
			}
			return expired;
		} catch (Exception e) {
			logger.error("Token süresi kontrol edilirken hata meydana geldi: {}", e.getMessage());
			return true;
		}
	}
}
