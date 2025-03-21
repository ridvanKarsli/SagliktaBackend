package com.saglikAdimiAPI.Model;

public class ContactInfo {

	private int contactID;
	private String email;
	private String phoneNumber;
	private int doctorID;

	public ContactInfo() {

	}

	public ContactInfo(int contactID, String email, String phoneNumber, int doctorID) {
		super();
		this.contactID = contactID;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.doctorID = doctorID;
	}

	public int getContactID() {
		return contactID;
	}

	public void setContactID(int contactID) {
		this.contactID = contactID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

}
