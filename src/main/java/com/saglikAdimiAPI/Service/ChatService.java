package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.ChatActionable;
import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Repository.ChatRespository;

@Service
public class ChatService implements ChatActionable {

	private final ChatRespository chatRespository;

	@Autowired
	public ChatService(ChatRespository chatRespository) {
		this.chatRespository = chatRespository;
	}

	@Override
	public ResponseEntity<String> addChat(Chats chat, String token) {
		// TODO Auto-generated method stub
		return chatRespository.addChat(chat, token);
	}

	@Override
	public ResponseEntity<String> deleteChat(int chatID, String token) {
		// TODO Auto-generated method stub
		return chatRespository.deleteChat(chatID, token);
	}

	@Override
	public ResponseEntity<List<Chats>> getChats(String token) {
		// TODO Auto-generated method stub
		return chatRespository.getChats(token);
	}

	@Override
	public ResponseEntity<List<Chats>> getAllChat(String token) {
		// TODO Auto-generated method stub
		return chatRespository.getAllChat(token);
	}

}
