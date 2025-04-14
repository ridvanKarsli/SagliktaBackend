package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Chats;

public interface ChatActionable {

	ResponseEntity<String> addChat(Chats chat, String token);

	ResponseEntity<String> deleteChat(int chatID, String token);

	ResponseEntity<List<Chats>> getChats(String token);

	ResponseEntity<List<Chats>> getAllChat(String token);
	
}
