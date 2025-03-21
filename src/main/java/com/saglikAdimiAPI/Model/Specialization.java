package com.saglikAdimiAPI.Model;

public class Specialization {

	private int specializationID;
	private String nameOfSpecialization;
	private int specializationExperience;
	private int doctorID;

	public Specialization() {

	}

	public Specialization(int specializationID, String nameOfSpecialization, int specializationExperience,
			int doctorID) {
		super();
		this.specializationID = specializationID;
		this.nameOfSpecialization = nameOfSpecialization;
		this.specializationExperience = specializationExperience;
		this.doctorID = doctorID;
	}

	public int getSpecializationID() {
		return specializationID;
	}

	public void setSpecializationID(int specializationID) {
		this.specializationID = specializationID;
	}

	public String getNameOfSpecialization() {
		return nameOfSpecialization;
	}

	public void setNameOfSpecialization(String nameOfSpecialization) {
		this.nameOfSpecialization = nameOfSpecialization;
	}

	public int getSpecializationExperience() {
		return specializationExperience;
	}

	public void setSpecializationExperience(int specializationExperience) {
		this.specializationExperience = specializationExperience;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

}
