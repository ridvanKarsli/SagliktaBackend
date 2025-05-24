package com.saglikAdimiAPI.Helper;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Repository.LogUserRepository;

@Service
public class EmailService {

	@Value("${email.sender}")
	private String senderEmail;

	@Value("${email.password}")
	private String senderPassword;

	@Value("${email.smtp.host}")
	private String host;

	@Value("${email.smtp.port}")
	private String port;

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
	
    @Autowired
    private LogUserRepository logUserRepository;

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
		
		
		boolean isUsable = logUserRepository.emailUsable(email);
		return isValidEmailFormat(email) && isEmailDomainValid(email) && isUsable;
	}

	public boolean isEmailValidForContact(String email) {
		return isValidEmailFormat(email) && isEmailDomainValid(email);
	}

	public boolean sendWelcomeMail(String email) {
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
			message.setSubject("Sağlıktan'a Hoşgeldiniz");
			message.setText("Merhaba,\n\n" + "SAĞLIKTAN platformuna hoş geldiniz!\n\n"
					+ "Artık kronik hastalıklar hakkında bilgi edinebilir, alanında uzman doktorlarla ve sizinle benzer deneyimler yaşamış kişilerle iletişim kurabilirsiniz.\n\n"
					+ "Platformumuzda şunları yapabilirsiniz:\n"
					+ "- Forumlarda diğer kullanıcılarla deneyimlerinizi paylaşmak,\n"
					+ "- Uzmanların yazdığı bilgilendirici içerikleri okumak,\n"
					+ "- Yapay zeka destekli sağlık önerileri almak,\n"
					+ "- Kendi sağlığınız için bilinçli adımlar atmak.\n\n"
					+ "İlk adım olarak profilinizi tamamlayarak ilgi alanlarınıza uygun içerikleri keşfetmeye başlayabilirsiniz.\n\n"
					+ "Sağlıklı ve bilinçli bir yolculuk için buradayız.\n"
					+ "Her türlü sorunuzda bizimle iletişime geçmekten çekinmeyin.\n\n" + "Sevgi ve sağlıkla,\n"
					+ "SAĞLIKTAN Ekibi");

			// E-postayı gönderme işlemi
			Transport.send(message);
			return true;
		} catch (MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}

}
