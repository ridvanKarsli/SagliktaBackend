package com.saglikAdimiAPI.Model;

import java.time.LocalDate;

public class Announcement {

	private int announcementID;
	private String title;
	private String content;
	private int doctorID;
	private LocalDate uploadDate;

	public Announcement() {

	}

	public Announcement(int announcementID, String title, String content, int doctorID, LocalDate uploadDate) {
		super();
		this.announcementID = announcementID;
		this.title = title;
		this.content = content;
		this.doctorID = doctorID;
		this.uploadDate = uploadDate;
	}

	public int getAnnouncementID() {
		return announcementID;
	}

	public void setAnnouncementID(int announcementID) {
		this.announcementID = announcementID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getDoctorID() {
		return doctorID;
	}

	public void setDoctorID(int doctorID) {
		this.doctorID = doctorID;
	}

	public LocalDate getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDate uploadDate) {
		this.uploadDate = uploadDate;
	}

}
