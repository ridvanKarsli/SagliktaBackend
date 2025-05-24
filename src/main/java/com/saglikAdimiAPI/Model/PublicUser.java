package com.saglikAdimiAPI.Model;

import java.time.LocalDate;
import java.util.List;
 
public class Patient extends Person {

	private List<Disease> diseases;

	public Patient() {
		super();
	}

	public Patient(int id, String name, String surname, LocalDate dateOfBirth, String role, String email,
			String password, List<Disease> diseases) {
		super(id, name, surname, role, email);
		// TODO Auto-generated constructor stub
		this.diseases = diseases;
	}

	public List<Disease> getDiseases() {
		return diseases;
	}

	public void setDiseases(List<Disease> diseases) {
		this.diseases = diseases;
	}

}