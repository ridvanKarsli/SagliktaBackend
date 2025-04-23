package com.saglikAdimiAPI.Abstraction;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Person;

public interface SagliktaAIActionable {

	String askSagliktaAI(String message, String token);
	
}
