package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ContactInfoActionable;
import com.saglikAdimiAPI.Helper.EmailService;
import com.saglikAdimiAPI.Model.ContactInfo;
import com.saglikAdimiAPI.Repository.ContactInfoRespository;

@Service
public class ContactInfoService implements ContactInfoActionable {

	private final ContactInfoRespository contactInfoResponsitory;

	@Autowired
	public ContactInfoService(ContactInfoRespository contactInfoResponsitory) {
		this.contactInfoResponsitory = contactInfoResponsitory;
	}

	@Override
	public ResponseEntity<String> addContactInfo(ContactInfo contactInfo, String token) {
		// TODO Auto-generated method stub
		if (!isContactUsable(contactInfo)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		return contactInfoResponsitory.addContactInfo(contactInfo, token);
	}

	@Override
	public ResponseEntity<String> updateContactInfo(ContactInfo contactInfo, String token) {
		// TODO Auto-generated method stub
		if (!isContactUsable(contactInfo)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		return contactInfoResponsitory.updateContactInfo(contactInfo, token);
	}

	@Override
	public ResponseEntity<ContactInfo> getContect(int ContactID, String token) {
		// TODO Auto-generated method stub
		return contactInfoResponsitory.getContect(ContactID, token);
	}

	@Override
	public ResponseEntity<List<ContactInfo>> getAllContect(int userID, String token) {
		// TODO Auto-generated method stub
		return contactInfoResponsitory.getAllContect(userID, token);
	}

	@Override
	public ResponseEntity<String> deleteContactInfo(int contactID, String token) {
		// TODO Auto-generated method stub
		return contactInfoResponsitory.deleteContactInfo(contactID, token);
	}
	
	private Boolean isContactUsable(ContactInfo contactInfo) {

	    String phoneNumber = contactInfo.getPhoneNumber();
	    String email = contactInfo.getEmail();

	    if (!isValidPhoneNumber(phoneNumber)) {
	        return false;
	    }

	    EmailService emailService = new EmailService();
	    if (!emailService.isEmailValidForContact(email)) {
	        return false;
	    }

	    return true;
	}

	// Telefon numarası doğrulama metodu
	private boolean isValidPhoneNumber(String phoneNumber) {
	    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
	        return false;
	    }
	    
	    // Örnek: 10 haneli ve sadece rakamlardan oluşan bir numara kontrolü
	    return phoneNumber.matches("\\d{10}");
	}

}
