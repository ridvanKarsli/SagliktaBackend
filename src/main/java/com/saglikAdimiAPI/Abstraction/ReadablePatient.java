package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Patient;

public interface ReadablePatient {

	ResponseEntity<List<Patient>> getAllPatient(String token);

	ResponseEntity<Patient> getPatient(int userID, String token);
}
