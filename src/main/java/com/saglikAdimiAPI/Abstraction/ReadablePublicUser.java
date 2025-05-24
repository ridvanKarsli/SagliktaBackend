package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.PublicUser;

public interface ReadablePublicUser {

	ResponseEntity<List<PublicUser>> getAllPublicUser(String token);

	ResponseEntity<PublicUser> getPublicUser(int userID, String token);
}
