package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.CommentsActionable;
import com.saglikAdimiAPI.Model.Comments;
import com.saglikAdimiAPI.Repository.CommentsRepository;

@Service
public class CommentsService implements CommentsActionable {

	private final CommentsRepository commentsRepository;

	public CommentsService(CommentsRepository commentsRepository) {
		this.commentsRepository = commentsRepository;
	}

	@Override
	public ResponseEntity<String> addComment(Comments comment, String token) {
		// TODO Auto-generated method stub
		if (!isCommentUsable(comment)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		return commentsRepository.addComment(comment, token);
	}

	@Override
	public ResponseEntity<String> deleteComment(int commnetsID, String token) {
		// TODO Auto-generated method stub
		return commentsRepository.deleteComment(commnetsID, token);
	}

	@Override
	public ResponseEntity<List<Comments>> getComments(int chatID, String token) {
		// TODO Auto-generated method stub
		return commentsRepository.getComments(chatID, token);
	}
	
	private Boolean isCommentUsable(Comments comment) {
	    String message = comment.getMessage();

	    if (message == null || message.trim().isEmpty() || message.length() > 500) {
	        return false;
	    }

	    return true;
	}

}
