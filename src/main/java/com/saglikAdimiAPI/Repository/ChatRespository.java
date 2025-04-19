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

import com.saglikAdimiAPI.Abstraction.ChatActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Chats;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class ChatRespository implements ChatActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
	private Connection conn;

	@Override
	public ResponseEntity<String> addChat(Chats chat, String token) {
		// TODO Auto-generated method stub
		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);
		System.out.print(person.getUserID());

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"Chats\"(\n"
				+ " message, \"likeCount\", \"dislikeCount\", \"uploadDate\", \"userID\")\n"
				+ "	VALUES (?, ?, ?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, chat.getMessage().trim());
			insert.setInt(2, chat.getLikeCount());
			insert.setInt(3, chat.getDislikeCount());
			insert.setDate(4, Date.valueOf(LocalDate.now()));
			insert.setInt(5, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();
			return new ResponseEntity<>("Sohbet eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteChat(int chatID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"Chats\" WHERE \"chatID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, chatID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("sohbet silindi");
				return new ResponseEntity<>("Sohbet silindi!", HttpStatus.OK);
			} else {
				System.out.println("Sohbet bulunamadı");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Sohbet bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Chats>> getChats(int userID, String token) {
		// TODO Auto-generated method stub

		List<Chats> chatList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"Chats\" WHERE \"userID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
	            if (!rs.next()) {
	                // Eğer hiçbir sonuç bulunmazsa 404 Not Found dön
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }
				Chats chat = new Chats();
				chat.setChatID(rs.getInt("chatID"));
				chat.setMessage(rs.getString("message").trim());
				chat.setLikeCount(rs.getInt("likeCount"));
				chat.setDislikeCount(rs.getInt("dislikeCount"));
				chat.setUploadDate(rs.getDate("uploadDate").toLocalDate());
				chat.setUserID(userID);

				CommentsRepository cr = new CommentsRepository();
				chat.setComments(cr.getComments(rs.getInt("chatID"), token).getBody());

				chatList.add(chat); // Listeye ekle
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.ok(chatList);
	}

	@Override
	public ResponseEntity<List<Chats>> getAllChat(String token) {
		// TODO Auto-generated method stub
		List<Chats> chatList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"Chats\"";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
	            if (!rs.next()) {
	                // Eğer hiçbir sonuç bulunmazsa 404 Not Found dön
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }
				Chats chat = new Chats();
				chat.setChatID(rs.getInt("chatID"));
				chat.setMessage(rs.getString("message").trim());
				chat.setLikeCount(rs.getInt("likeCount"));
				chat.setDislikeCount(rs.getInt("dislikeCount"));
				chat.setUploadDate(rs.getDate("uploadDate").toLocalDate());
				chat.setUserID(rs.getInt("userID"));

				CommentsRepository cr = new CommentsRepository();
				chat.setComments(cr.getComments(rs.getInt("chatID"), token).getBody());

				chatList.add(chat); // Listeye ekle
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.ok(chatList);
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
