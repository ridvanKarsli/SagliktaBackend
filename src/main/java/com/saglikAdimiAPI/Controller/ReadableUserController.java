package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.ReadablePerson;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Person;
import com.saglikAdimiAPI.Model.Specialization;
import com.saglikAdimiAPI.Service.ReadableUserService;

@RestController
@RequestMapping("/user")
public class ReadableUserController implements ReadablePerson {

	private final ReadableUserService readableUserService;

	JwtService jwtService = new JwtService();

	@Autowired
	public ReadableUserController(ReadableUserService readableUserService) {
		this.readableUserService = readableUserService;
	}

	@GetMapping("/users")
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<List<Person>> getAllPerson(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return readableUserService.getAllPerson(token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

	}

}
