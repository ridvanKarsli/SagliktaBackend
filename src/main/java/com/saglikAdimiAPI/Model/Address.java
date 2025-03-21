package com.saglikAdimiAPI.Model;

public class Address {

	private int adressID;
	private String workPlaceName; // işyeri adı
	private String street; // Sokak adı ve numarası
	private String city; // Şehir
	private String county; // ilçe
	private String country; // Ülke
	private int doctorID;

	public Address() {

	}

	public Address(int adressID, String workPlaceName, String street, String city, String county, String country,
			int doctorID) {
		super();
		this.adressID = adressID;
		this.workPlaceName = workPlaceName;
		this.street = street;
		this.city = city;
		this.county = county;
		this.country = country;
		this.doctorID = doctorID;
	}

	public int getAdressID() {
		return adressID;
	}

	public void setAdressID(int adressID) {
		this.adressID = adressID;
	}

	public String getWorkPlaceName() {
		return workPlaceName;
	}

	public void setWorkPlaceName(String workPlaceName) {
		this.workPlaceName = workPlaceName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}
}
