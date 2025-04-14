package com.saglikAdimiAPI.Abstraction;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Person;

public interface Logable<T> {

	ResponseEntity<String> login(Person person);

	ResponseEntity<String> SignUp(Person person);	
	
	/*
	ResponseEntity<String> sendVerificationCode(String email);
	
	ResponseEntity<String> verificationEmail(String email, String code);
	*/

}
