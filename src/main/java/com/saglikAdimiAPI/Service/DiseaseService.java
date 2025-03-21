package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.DiseaseActionable;
import com.saglikAdimiAPI.Model.Disease;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Repository.DiseaseRepository;

@Service
public class DiseaseService implements DiseaseActionable<Patient> {

	private final DiseaseRepository diseaseRepository;

	@Autowired
	public DiseaseService(DiseaseRepository diseaseRepository) {
		this.diseaseRepository = diseaseRepository;
	}

	@Override
	public ResponseEntity<String> addDisease(Disease disease, String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.addDisease(disease, token);
	}

	@Override
	public ResponseEntity<List<Disease>> getDiseases(int userID, String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.getDiseases(userID, token);
	}

	@Override
	public ResponseEntity<Disease> getDisease(int diseaseID, String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.getDisease(diseaseID, token);
	}

	@Override
	public ResponseEntity<String> deleteDisease(int diseaseID, String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.deleteDisease(diseaseID, token);
	}

	@Override
	public ResponseEntity<String> updateDisease(Disease disease, String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.updateDisease(disease, token);
	}

	@Override
	public ResponseEntity<List<String>> getDiseaseNames(String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.getDiseaseNames(token);
	}

}
