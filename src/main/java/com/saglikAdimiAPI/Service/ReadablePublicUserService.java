package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ReadablePublicUser;
import com.saglikAdimiAPI.Model.PublicUser;
import com.saglikAdimiAPI.Repository.ReadablePublicUserRepository;

@Service
public class ReadablePublicUserService implements ReadablePublicUser {

	private final ReadablePublicUserRepository readablePatientRepository;

	public ReadablePublicUserService(ReadablePublicUserRepository readablePatientRepository) {
		this.readablePatientRepository = readablePatientRepository;
	}

	@Override
	public ResponseEntity<List<PublicUser>> getAllPublicUser(String token) {
		// TODO Auto-generated method stub
		return readablePatientRepository.getAllPublicUser(token);
	}

	@Override
	public ResponseEntity<PublicUser> getPublicUser(int userID, String token) {
		// TODO Auto-generated method stub
		return readablePatientRepository.getPublicUser(userID, token);
	}

}
