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

import com.saglikAdimiAPI.Abstraction.ReadablePublicUser;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.PublicUser;
import com.saglikAdimiAPI.Service.ReadablePublicUserService;

@RestController
@RequestMapping("/publicUser")
public class ReadablePublicUserController implements ReadablePublicUser {

	private final ReadablePublicUserService readablePatientService;

	private final JwtService jwtService;

	@Autowired
	public ReadablePublicUserController(ReadablePublicUserService readablePatientService, JwtService jwtService) {
		this.readablePatientService = readablePatientService;
		this.jwtService = jwtService;
	}

	@GetMapping("/publicUsers")
	@Override
	public ResponseEntity<List<PublicUser>> getAllPublicUser(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readablePatientService.getAllPublicUser(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/publicUser")
	@Override
	public ResponseEntity<PublicUser> getPublicUser(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readablePatientService.getPublicUser(userID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
