package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ReadableDoctor;
import com.saglikAdimiAPI.Model.Doctor;
import com.saglikAdimiAPI.Repository.ReadableDoctorRepository;

@Service
public class ReadableDoctorService implements ReadableDoctor {

	private final ReadableDoctorRepository readableDoctorRepository;

	@Autowired
	public ReadableDoctorService(ReadableDoctorRepository readableDoctorRepository) {
		this.readableDoctorRepository = readableDoctorRepository;
	}

	@Override
	public ResponseEntity<Doctor> getDoctor(int userID, String token) {
		// TODO Auto-generated method stub
		return readableDoctorRepository.getDoctor(userID, token);
	}

	@Override
	public ResponseEntity<List<Doctor>> getAllDoctor(String token) {
		// TODO Auto-generated method stub
		return readableDoctorRepository.getAllDoctor(token);
	}

}
