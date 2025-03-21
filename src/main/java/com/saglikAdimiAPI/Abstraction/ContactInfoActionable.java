package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.ContactInfo;

public interface ContactInfoActionable {

	ResponseEntity<String> addContactInfo(ContactInfo contactInfo, String token);

	ResponseEntity<String> updateContactInfo(ContactInfo contactInfo, String token);

	ResponseEntity<String> deleteContactInfo(int contactID, String token);

	ResponseEntity<ContactInfo> getContect(int contactID, String token);

	ResponseEntity<List<ContactInfo>> getAllContect(int userID, String token);

}
