package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.ContactInfoActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.ContactInfo;
import com.saglikAdimiAPI.Service.ContactInfoService;

@RestController
@RequestMapping("/contactInfor")
public class ContactInfoController implements ContactInfoActionable {

	private final ContactInfoService contactInfoService;

	private final JwtService jwtService;

	@Autowired
	public ContactInfoController(ContactInfoService contactInfoService, JwtService jwtService) {
		this.contactInfoService = contactInfoService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addContactInfor")
	@Override
	public ResponseEntity<String> addContactInfo(@RequestBody ContactInfo contactInfo,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return contactInfoService.addContactInfo(contactInfo, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@PutMapping("/updateContactInfor")
	@Override
	public ResponseEntity<String> updateContactInfo(@RequestBody ContactInfo contactInfo,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return contactInfoService.updateContactInfo(contactInfo, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getContact")
	@Override
	public ResponseEntity<ContactInfo> getContect(@RequestParam int contactID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return contactInfoService.getContect(contactID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/getAllContact")
	@Override
	public ResponseEntity<List<ContactInfo>> getAllContect(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return contactInfoService.getAllContect(userID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@DeleteMapping("/deleteContact")
	@Override
	public ResponseEntity<String> deleteContactInfo(@RequestParam int contactID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return contactInfoService.deleteContactInfo(contactID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

}
