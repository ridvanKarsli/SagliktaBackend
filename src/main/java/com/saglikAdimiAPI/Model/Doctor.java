package com.saglikAdimiAPI.Model;

import java.time.LocalDate;
import java.util.List;

public class Doctor extends Person {

	private List<Specialization> specialization;
	private List<Address> worksAddress;
	private List<ContactInfo> contactInfor;
	private List<Announcement> announcement;

	public Doctor() {

	}

	public Doctor(int id, String name, String surname, LocalDate dateOfBirth, String role, String email,
			String password, List<Specialization> specialization, List<Address> worksAddress,
			List<ContactInfo> contactInfor, List<Announcement> announcement) {
		super(id, name, surname, role, email);
		// TODO Auto-generated constructor stub
		this.specialization = specialization;
		this.worksAddress = worksAddress;
		this.contactInfor = contactInfor;
		this.announcement = announcement;
	}

	public List<Specialization> getSpecialization() {
		return specialization;
	}

	public void setSpecialization(List<Specialization> specialization) {
		this.specialization = specialization;
	}

	public List<Address> getWorksAddress() {
		return worksAddress;
	}

	public void setWorksAddress(List<Address> worksAddress) {
		this.worksAddress = worksAddress;
	}

	public List<ContactInfo> getContactInfor() {
		return contactInfor;
	}

	public void setContactInfor(List<ContactInfo> contactInfor) {
		this.contactInfor = contactInfor;
	}

	public List<Announcement> getAnnouncement() {
		return announcement;
	}

	public void setAnnouncement(List<Announcement> announcement) {
		this.announcement = announcement;
	}

}
