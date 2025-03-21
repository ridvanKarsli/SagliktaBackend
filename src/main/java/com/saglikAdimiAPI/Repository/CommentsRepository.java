package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.CommentsActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Model.Comments;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class CommentsRepository implements CommentsActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5433/SaglikAdimi_db?user=postgres&password=3519";
	private Connection conn;
	//test

	@Override
	public ResponseEntity<String> addComment(Comments comment, String token) {
		// TODO Auto-generated method stub

		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);
		System.out.print(person.getUserID());

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"Comments\"(\n"
				+ " message, \"likeCount\", \"dislikeCount\", \"uploadDate\", \"chatID\", \"userID\")\n"
				+ "	VALUES (?, ?, ?, ?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, comment.getMessage().trim());
			insert.setInt(2, comment.getLikeCount());
			insert.setInt(3, comment.getDislikeCount());
			insert.setDate(4, Date.valueOf(LocalDate.now()));
			insert.setInt(5, comment.getChatID());
			insert.setInt(6, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();
			return new ResponseEntity<>("Yorum eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteComment(int commnetsID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"Comments\" WHERE \"commnetsID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, commnetsID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("yorum silindi");
				return new ResponseEntity<>("yorum silindi!", HttpStatus.OK);
			} else {
				System.out.println("yorum bulunamadı");
				ps.close();
				conn.close();
				return new ResponseEntity<>("yorum bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Comments>> getComments(int chatID, String token) {
		// TODO Auto-generated method stub

		List<Comments> commentList = new ArrayList<>();
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"Comments\" WHERE \"chatID\" = ?";

		try { 
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setInt(1, chatID);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				Comments comment = new Comments();
				comment.setCommnetsID(rs.getInt("commnetsID"));
				comment.setMessage(rs.getString("message").trim());
				comment.setLikeCount(rs.getInt("likeCount"));
				comment.setDislikeCount(rs.getInt("dislikeCount"));
				comment.setUploadDate(rs.getDate("uploadDate").toLocalDate());
				comment.setChatID(rs.getInt("chatID"));
				comment.setUserID(rs.getInt("userID"));

				commentList.add(comment);
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(commentList);
	}

	private void getConnection() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
