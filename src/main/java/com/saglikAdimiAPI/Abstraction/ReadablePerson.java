package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Person;

public interface ReadablePerson {

	ResponseEntity<List<Person>> getAllPerson(String token);
	
	ResponseEntity<Person> getPerson(int userID, String token);
	
	ResponseEntity<Person> getLoggedPerson(String token);

}