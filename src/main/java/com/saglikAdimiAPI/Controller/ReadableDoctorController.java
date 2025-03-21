package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.ReadableDoctor;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Address;
import com.saglikAdimiAPI.Model.Doctor;
import com.saglikAdimiAPI.Service.ReadableDoctorService;

@RestController
@RequestMapping("/doctor")
public class ReadableDoctorController implements ReadableDoctor {

	private ReadableDoctorService readableDoctorService;

	JwtService jwtService = new JwtService();

	@Autowired
	public ReadableDoctorController(ReadableDoctorService readableDoctorService) {
		this.readableDoctorService = readableDoctorService;
	}

	@GetMapping("/doctors")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<List<Doctor>> getAllDoctor(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readableDoctorService.getAllDoctor(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/doctor")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Doctor> getDoctor(@RequestParam int userID, @RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readableDoctorService.getDoctor(userID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
