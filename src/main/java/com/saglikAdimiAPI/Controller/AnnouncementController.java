package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.AnnouncementActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Announcement;
import com.saglikAdimiAPI.Service.AnnouncementService;

@RestController
@RequestMapping("/announcement")
public class AnnouncementController implements AnnouncementActionable {

	private final AnnouncementService announcementService;

	private final JwtService jwtService;

	@Autowired
	public AnnouncementController(AnnouncementService announcementService, JwtService jwtService) {
		this.announcementService = announcementService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addAnnouncement")
	@Override
	public ResponseEntity<String> addAnnouncement(@RequestBody Announcement announcement,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return announcementService.addAnnouncement(announcement, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@DeleteMapping("/deleteAnnouncement")
	@Override
	public ResponseEntity<String> deleteAnnouncement(@RequestParam int announcementID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return announcementService.deleteAnnouncement(announcementID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getAnnouncement")
	@Override
	public ResponseEntity<Announcement> getAnnouncement(@RequestParam int announcementID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {

			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return announcementService.getAnnouncement(announcementID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			// Token süresi dolmuşsa veya geçersizse, Unauthorized hata mesajı döndürüyoruz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // veya .body("Token is invalid or
																				// expired") mesajı da dönebilir
		}

	}

	@GetMapping("/getAnnouncements")
	@Override
	public ResponseEntity<List<Announcement>> getAnnouncements(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return announcementService.getAnnouncements(userID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // veya .body("Token is invalid or
			}

		} else {
			// Token süresi dolmuşsa veya geçersizse, Unauthorized hata mesajı döndürüyoruz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // veya .body("Token is invalid or
		}
	}

}
