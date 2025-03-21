package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.ReadablePatient;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Service.ReadablePatientService;

@RestController
@RequestMapping("/patient")
public class ReadablePatientController implements ReadablePatient {

	private ReadablePatientService readablePatientService;

	JwtService jwtService = new JwtService();

	@Autowired
	public ReadablePatientController(ReadablePatientService readablePatientService) {
		this.readablePatientService = readablePatientService;
	}

	@GetMapping("/patients")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<List<Patient>> getAllPatient(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readablePatientService.getAllPatient(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/patient")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<Patient> getPatient(@RequestParam int userID, @RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readablePatientService.getPatient(userID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
