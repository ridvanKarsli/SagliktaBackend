package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Specialization;

public interface SpecializationActionable {

	ResponseEntity<String> addSpecialization(Specialization specialization, String token);

	ResponseEntity<String> deleteSpecialization(int specializationID, String token);

	ResponseEntity<String> updateSpecialization(Specialization specialization, String token);

	ResponseEntity<List<Specialization>> getSpecializations(int userID, String token);

	ResponseEntity<Specialization> getSpecialization(int specializationID, String token);
}
