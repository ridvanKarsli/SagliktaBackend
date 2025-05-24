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

import com.saglikAdimiAPI.Abstraction.SpecializationActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Specialization;
import com.saglikAdimiAPI.Service.SpecializationService;

@RestController
@RequestMapping("/specialization")
public class SpecializationController implements SpecializationActionable {

	private final SpecializationService specializationService;

	private final JwtService jwtService;

	@Autowired
	public SpecializationController(SpecializationService specializationService, JwtService jwtService) {
		this.specializationService = specializationService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addSpecialization")
	@Override
	public ResponseEntity<String> addSpecialization(@RequestBody Specialization specialization,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return specializationService.addSpecialization(specialization, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@DeleteMapping("/deleteSpecialization")
	@Override
	public ResponseEntity<String> deleteSpecialization(@RequestParam int specializationID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return specializationService.deleteSpecialization(specializationID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@PutMapping("/updateSpecialization")
	@Override
	public ResponseEntity<String> updateSpecialization(@RequestBody Specialization specialization,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return specializationService.updateSpecialization(specialization, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getSpecializations")
	@Override
	public ResponseEntity<List<Specialization>> getSpecializations(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return specializationService.getSpecializations(userID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/getSpecialization")
	@Override
	public ResponseEntity<Specialization> getSpecialization(@RequestParam int specializationID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return specializationService.getSpecialization(specializationID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
