package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ContactInfoActionable;
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
		return contactInfoResponsitory.addContactInfo(contactInfo, token);
	}

	@Override
	public ResponseEntity<String> updateContactInfo(ContactInfo contactInfo, String token) {
		// TODO Auto-generated method stub
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

}
