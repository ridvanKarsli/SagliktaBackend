package com.saglikAdimiAPI.Model;

import java.time.LocalDate;

public class Person {

	private int userID;
	private String name;
	private String surname;
	private LocalDate dateOfBirth;
	private String role;
	private String email;
	private String password;

	public Person() {
	}

	public Person(int userID, String name, String surname, String email, String role) {
		this.userID = userID;
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.role = role;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
