package com.saglikAdimiAPI.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session; // ya da
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Repository.LogUserRepository;

@Service
public class EmailService {

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
	private Map<String, String> verificationCodes = new HashMap<>();

	// Regex ile e-posta doğrulama
	private boolean isValidEmailFormat(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}

	// MX kaydı kontrolü ile e-posta doğrulama (daha güvenilir)
	private boolean isEmailDomainValid(String email) {
		String domain = email.substring(email.indexOf("@") + 1);
		try {
			DirContext ctx = new InitialDirContext(new Properties());
			Attributes attrs = ctx.getAttributes("dns:/" + domain, new String[] { "MX" });
			return attrs.get("MX") != null;
		} catch (NamingException e) {
			return false; // Domain geçersizse veya MX kaydı yoksa
		}
	}

	// E-posta adresinin genel geçer olup olmadığını kontrol eden ana metod
	public boolean isEmailValid(String email) {
		LogUserRepository logUserRepository = new LogUserRepository();
		boolean isUsable = logUserRepository.emailUsable(email);
		return isValidEmailFormat(email) && isEmailDomainValid(email) && isUsable;
	}
	
	public boolean isEmailValidForContact(String email) {		
		return isValidEmailFormat(email) && isEmailDomainValid(email);
	}
	


	public boolean sendVerificationCode(String email) {
		// Doğrulama kodu oluşturma
		String verificationCode = generateVerificationCode();
		verificationCodes.put(email.trim(), verificationCode); 
		System.out.println("Doğrulama kodu kaydedildi: " + email + " -> " + verificationCode);		

		// E-posta ayarları
		String host = "smtp.gmail.com";
		String port = "587";
		String senderEmail = "rdvn35050@gmail.com"; // Gönderen e-posta adresi
		String senderPassword = "vqpq xvrk jwix hesj"; // Uygulama şifresi

		// E-posta bağlantısı için gerekli ayarları yapılandırıyoruz
		Properties properties = new Properties();
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port", port);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");

		// E-posta oturumunu oluşturma
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(senderEmail, senderPassword);
			}
		});

		try {
			// E-posta mesajını oluşturma
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(senderEmail));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("Med Connect uygulamasına kayıt olmak istediniz. E-posta Doğrulama Kodu");
			message.setText("Doğrulama kodunuz: " + verificationCode);

			// E-postayı gönderme işlemi
			Transport.send(message);
			System.out.println("Doğrulama kodu e-posta ile gönderildi.");
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
    public boolean verifyCode(String email, String userCode) {
        String storedCode = verificationCodes.get(email.trim());
        System.out.print("input code :"+ userCode + "email code : "+storedCode);
        
        if (storedCode != null && storedCode.equals(userCode)) {        	
            verificationCodes.remove(email); // Kod doğrulandıktan sonra sil
            return true;
        }
        return false;
    }	

	// Doğrulama kodu oluşturma (6 haneli rastgele sayı)
	private String generateVerificationCode() {
		Random random = new Random();
		int code = 100000 + random.nextInt(900000); // 100000 ile 999999 arasında rastgele sayı
		return String.valueOf(code); // Sayıyı String'e dönüştürüp döndür
	}
	
	
}
