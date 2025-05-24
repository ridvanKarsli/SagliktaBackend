package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.ChatActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Service.ChatService;

@RestController
@RequestMapping("/chats")
public class ChatController implements ChatActionable {

	private final JwtService jwtService;

	private final ChatService chatService;

	@Autowired
	public ChatController(ChatService chatService, JwtService jwtService) {
		this.chatService = chatService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addChat")
	@Override
	public ResponseEntity<String> addChat(@RequestBody Chats chat, @RequestHeader("Authorization") String token) {
		// Token geçerliliğini kontrol et
		if (!jwtService.isTokenExpired(token)) {
			return chatService.addChat(chat, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@DeleteMapping("/deleteChat")
	@Override
	public ResponseEntity<String> deleteChat(@RequestParam int chatID, @RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub
		if (!jwtService.isTokenExpired(token)) {
			return chatService.deleteChat(chatID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getChats")
	@Override
	public ResponseEntity<List<Chats>> getChats(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return chatService.getChats(userID, token);
		} else {
			// Token süresi dolmuşsa veya geçersizse, Unauthorized hata mesajı döndürüyoruz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // veya .body("Token is invalid or
																				// expired") mesajı da dönebilir
		}
	}

	@GetMapping("/getAllChat")
	@Override
	public ResponseEntity<List<Chats>> getAllChat(@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return chatService.getAllChat(token);
		} else {
			// Token süresi dolmuşsa veya geçersizse, Unauthorized hata mesajı döndürüyoruz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}

	}

	@GetMapping("/getChatsWithFiltre")
	@Override
	public ResponseEntity<List<Chats>> getChatsWithFiltre(@RequestParam String category,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub
		if (!jwtService.isTokenExpired(token)) {
			return chatService.getChatsWithFiltre(token, category);
		} else {
			// Token süresi dolmuşsa veya geçersizse, Unauthorized hata mesajı döndürüyoruz
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
