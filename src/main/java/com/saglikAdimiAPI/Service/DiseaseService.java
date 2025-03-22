package com.saglikAdimiAPI.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		if(!isDiseaseUsable(disease)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
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
		if(!isDiseaseUsable(disease)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		return diseaseRepository.updateDisease(disease, token);
	}

	@Override
	public ResponseEntity<List<String>> getDiseaseNames(String token) {
		// TODO Auto-generated method stub
		return diseaseRepository.getDiseaseNames(token);
	}
	
	
    public Boolean isDiseaseUsable(Disease disease) {
        // Eğer hastalık adı boş ise
        if (disease.getDiseaseName() == null || disease.getDiseaseName().isEmpty()) {
            return false; // Hastalık adı boş olamaz
        }

        // Eğer dateOfDiagnosis null ise veya tarih formatı yanlışsa
        if (disease.getDateOfDiagnosis() == null) {
            return false; // Date of Diagnosis null olamaz
        }

        // Tarih formatını kontrol et
        try {
            LocalDate diagnosisDate = LocalDate.parse(disease.getDateOfDiagnosis().toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return false; // Eğer tarih formatı yanlışsa
        }

        return true; // Eğer her şey uygunsa
    }

}
