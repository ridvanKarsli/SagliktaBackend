package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Disease;

public interface DiseaseActionable<T> {

	ResponseEntity<String> addDisease(Disease disease, String token);

	ResponseEntity<List<Disease>> getDiseases(int userID, String token);

	ResponseEntity<Disease> getDisease(int diseaseID, String token);

	ResponseEntity<String> deleteDisease(int diseaseID, String token);

	ResponseEntity<String> updateDisease(Disease disease, String token);

	ResponseEntity<List<String>> getDiseaseNames(String token);

}
