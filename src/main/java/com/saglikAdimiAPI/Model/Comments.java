package com.saglikAdimiAPI.Model;

import java.time.LocalDate;

public class Comments {

	private int commnetsID;
	private String message;
	private int likeCount;
	private int dislikeCount;
	private LocalDate uploadDate;
	private int chatID;
	private int userID;

	public Comments() {

	}

	public Comments(int id, String message, int likeCount, int dislikeCount, LocalDate uploadDate, int chatID,
			int userID) {
		super();
		this.commnetsID = id;
		this.message = message;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.uploadDate = uploadDate;
		this.chatID = chatID;
		this.userID = userID;
	}

	public int getCommnetsID() {
		return commnetsID;
	}

	public void setCommnetsID(int id) {
		this.commnetsID = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}

	public LocalDate getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDate uploadDate) {
		this.uploadDate = uploadDate;
	}

	public int getChatID() {
		return chatID;
	}

	public void setChatID(int chatID) {
		this.chatID = chatID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

}
