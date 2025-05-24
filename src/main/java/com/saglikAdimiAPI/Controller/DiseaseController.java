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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.DiseaseActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Disease;
import com.saglikAdimiAPI.Model.PublicUser;
import com.saglikAdimiAPI.Service.DiseaseService;

@RestController
@RequestMapping("/disease")
public class DiseaseController implements DiseaseActionable<PublicUser> {

	private final DiseaseService diseaseService;

	private final JwtService jwtService;

	@Autowired
	public DiseaseController(DiseaseService diseaseService, JwtService jwtService) {
		this.diseaseService = diseaseService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addDisease")
	@Override
	public ResponseEntity<String> addDisease(@RequestBody Disease disease,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {

			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.addDisease(disease, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getDiseases")
	@Override
	public ResponseEntity<List<Disease>> getDiseases(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.getDiseases(userID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/getDisease")
	@Override
	public ResponseEntity<Disease> getDisease(@RequestParam int diseaseID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.getDisease(diseaseID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@DeleteMapping("/deleteDisease")
	@Override
	public ResponseEntity<String> deleteDisease(@RequestParam int diseaseID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.deleteDisease(diseaseID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@PutMapping("/updateDisease")
	@Override
	public ResponseEntity<String> updateDisease(@RequestBody Disease disease,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.updateDisease(disease, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getDiseaseNames")
	@Override
	public ResponseEntity<List<String>> getDiseaseNames(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub
		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().trim().equals("HASTA")) {
				return diseaseService.getDiseaseNames(token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
