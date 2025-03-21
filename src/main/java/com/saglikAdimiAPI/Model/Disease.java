package com.saglikAdimiAPI.Model;

import java.time.LocalDate;

public class Disease {

	private int diseaseID;
	private String diseaseName;
	// private DiseaseName diseaseName;
	private LocalDate dateOfDiagnosis;
	private int userID;

	public Disease() {

	}

	public Disease(int diseaseID, String diseaseName, LocalDate dateOfDiagnosis, int userID) {
		super();
		this.diseaseID = diseaseID;
		this.diseaseName = diseaseName;
		this.dateOfDiagnosis = dateOfDiagnosis;
		this.userID = userID;
	}

	public int getDiseaseID() {
		return diseaseID;
	}

	public void setDiseaseID(int diseaseID) {
		this.diseaseID = diseaseID;
	}

	public String getDiseaseName() {
		return diseaseName;
	}

	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}

	public LocalDate getDateOfDiagnosis() {
		return dateOfDiagnosis;
	}

	public void setDateOfDiagnosis(LocalDate dateOfDiagnosis) {
		this.dateOfDiagnosis = dateOfDiagnosis;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
