package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.SpecializationActionable;
import com.saglikAdimiAPI.Model.Specialization;
import com.saglikAdimiAPI.Repository.SpecializationRepository;

@Service
public class SpecializationService implements SpecializationActionable {

	private final SpecializationRepository specializationRepository;

	@Autowired
	public SpecializationService(SpecializationRepository specializationRepository) {
		this.specializationRepository = specializationRepository;
	}

	@Override
	public ResponseEntity<String> addSpecialization(Specialization specialization, String token) {
		// TODO Auto-generated method stub
		return specializationRepository.addSpecialization(specialization, token);
	}

	@Override
	public ResponseEntity<String> deleteSpecialization(int specializationID, String token) {
		// TODO Auto-generated method stub
		return specializationRepository.deleteSpecialization(specializationID, token);
	}

	@Override
	public ResponseEntity<String> updateSpecialization(Specialization specialization, String token) {
		// TODO Auto-generated method stub
		return specializationRepository.updateSpecialization(specialization, token);
	}

	@Override
	public ResponseEntity<List<Specialization>> getSpecializations(int userID, String token) {
		// TODO Auto-generated method stub
		return specializationRepository.getSpecializations(userID, token);
	}

	@Override
	public ResponseEntity<Specialization> getSpecialization(int specializationID, String token) {
		// TODO Auto-generated method stub
		return specializationRepository.getSpecialization(specializationID, token);
	}

}
