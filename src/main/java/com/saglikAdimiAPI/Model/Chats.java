package com.saglikAdimiAPI.Model;

import java.time.LocalDate;
import java.util.List;

public class Chats {

	private int chatID;
	private String message;
	private int likeCount;
	private int dislikeCount;
	private LocalDate uploadDate;
	private int userID;	
	private String category;
	private List<Comments> comments;

	public Chats() {

	}

	public Chats(int chatID, String message, int likeCount, int dislikeCount, LocalDate uploadDate, int userID, String category,  
			List<Comments> comments) {
		super();
		this.chatID = chatID;
		this.message = message;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
		this.uploadDate = uploadDate;
		this.userID = userID;
		this.category = category;
		this.comments = comments;
	}

	public int getChatID() {
		return chatID;
	}

	public void setChatID(int chatID) {
		this.chatID = chatID;
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

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}

	public List<Comments> getComments() {
		return comments;
	}

	public void setComments(List<Comments> comments) {
		this.comments = comments;
	}

}
