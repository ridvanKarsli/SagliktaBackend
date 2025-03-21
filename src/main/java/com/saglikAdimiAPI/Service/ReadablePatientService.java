package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ReadablePatient;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Repository.ReadablePatientRepository;

@Service
public class ReadablePatientService implements ReadablePatient {

	private final ReadablePatientRepository readablePatientRepository;

	public ReadablePatientService(ReadablePatientRepository readablePatientRepository) {
		this.readablePatientRepository = readablePatientRepository;
	}

	@Override
	public ResponseEntity<List<Patient>> getAllPatient(String token) {
		// TODO Auto-generated method stub
		return readablePatientRepository.getAllPatient(token);
	}

	@Override
	public ResponseEntity<Patient> getPatient(int userID, String token) {
		// TODO Auto-generated method stub
		return readablePatientRepository.getPatient(userID, token);
	}

}
