package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.CommentsActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Comments;
import com.saglikAdimiAPI.Service.CommentsService;

@RestController
@RequestMapping("/comments")
public class CommentsController implements CommentsActionable {

	private final CommentsService CommentsService;

	JwtService jwtService = new JwtService();

	@Autowired
	public CommentsController(CommentsService CommentsService) {
		this.CommentsService = CommentsService;
	}

	@PostMapping("/addComment")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<String> addComment(@RequestBody Comments comment,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return CommentsService.addComment(comment, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@PostMapping("/deleteComment")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<String> deleteComment(@RequestParam int commnetsID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return CommentsService.deleteComment(commnetsID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getComment")
	@ResponseStatus(HttpStatus.OK)
	@Override
	public ResponseEntity<List<Comments>> getComments(@RequestParam int chatID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			return CommentsService.getComments(chatID, token);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
