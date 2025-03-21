package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Model.Comments;

public interface CommentsActionable {

	ResponseEntity<String> addComment(Comments comment, String token);

	ResponseEntity<String> deleteComment(int commnetsID, String token);

	ResponseEntity<List<Comments>> getComments(int chatID, String token);
}
