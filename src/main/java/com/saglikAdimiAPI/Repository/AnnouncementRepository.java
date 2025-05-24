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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.AnnouncementActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Announcement;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class AnnouncementRepository implements AnnouncementActionable {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	private Connection conn;

	@Override
	public ResponseEntity<String> addAnnouncement(Announcement announcementRequest, String token) {
		// TODO Auto-generated method stub
		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);
		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"Announcement\"(\n" + "	title, content, \"userID\", \"uploadDate\")\n"
				+ "	VALUES (?, ?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, announcementRequest.getTitle().trim());
			insert.setString(2, announcementRequest.getContent().trim());
			insert.setInt(3, person.getUserID());
			insert.setDate(4, Date.valueOf(LocalDate.now()));

			insert.executeUpdate();
			insert.close();
			conn.close();
			return new ResponseEntity<>("Duyuru eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteAnnouncement(int announcementID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"Announcement\" WHERE \"announcementID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, announcementID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("Duyuru silindi");
				return new ResponseEntity<>("Duyuru silindi!", HttpStatus.OK);
			} else {
				System.out.println("duyuru bulunamadı");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Duyuru bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<Announcement> getAnnouncement(int announcementID, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını al
		Announcement announcement = null; // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"Announcement\" WHERE \"announcementID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, announcementID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					announcement = new Announcement();

					announcement.setAnnouncementID(rs.getInt("announcementID"));
					announcement.setTitle(rs.getString("title").trim());
					announcement.setContent(rs.getString("content").trim());
					announcement.setUploadDate(rs.getDate("uploadDate").toLocalDate());
					announcement.setDoctorID(rs.getInt("userID"));

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.ok(announcement);
	}

	@Override
	public ResponseEntity<List<Announcement>> getAnnouncements(int userID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// SQL sorgusu
		String query = "SELECT * FROM public.\"Announcement\" WHERE \"userID\" = ?;";
		List<Announcement> announcementList = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Sorguya parametreyi ata
			stmt.setInt(1, userID);

			// Sorguyu çalıştır
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					Announcement announcement = new Announcement();

					announcement.setAnnouncementID(rs.getInt("announcementID"));
					announcement.setTitle(rs.getString("title").trim());
					announcement.setContent(rs.getString("content").trim());
					announcement.setUploadDate(rs.getDate("uploadDate").toLocalDate());
					announcement.setDoctorID(rs.getInt("userID"));

					// Listeye ekle
					announcementList.add(announcement);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}

		return ResponseEntity.ok(announcementList);
	}

	private void getConnection() {
		try {
			conn = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
