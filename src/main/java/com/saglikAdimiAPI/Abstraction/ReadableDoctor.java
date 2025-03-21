package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Doctor;

public interface ReadableDoctor {

	ResponseEntity<List<Doctor>> getAllDoctor(String token);

	ResponseEntity<Doctor> getDoctor(int userID, String token);

}
