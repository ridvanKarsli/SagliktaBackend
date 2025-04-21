package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ReadablePerson;
import com.saglikAdimiAPI.Model.Person;
import com.saglikAdimiAPI.Repository.ReadableUserRepository;

@Service
public class ReadableUserService implements ReadablePerson {

	private final ReadableUserRepository readableUserRepository;

	@Autowired
	public ReadableUserService(ReadableUserRepository readableUserRepository) {
		this.readableUserRepository = readableUserRepository;
	}

	@Override
	public ResponseEntity<List<Person>> getAllPerson(String token) {
		// TODO Auto-generated method stub
		return readableUserRepository.getAllPerson(token);
	}

	@Override
	public ResponseEntity<Person> getLoggedPerson(String token) {
		// TODO Auto-generated method stub
		return readableUserRepository.getLoggedPerson(token);
	}

	@Override
	public ResponseEntity<Person> getPerson(int userID, String token) {
		// TODO Auto-generated method stub
		return readableUserRepository.getPerson(userID, token);
	}

}
