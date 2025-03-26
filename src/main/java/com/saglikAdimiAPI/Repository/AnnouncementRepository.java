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

import com.saglikAdimiAPI.Abstraction.AnnouncementActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Announcement;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class AnnouncementRepository implements AnnouncementActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
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

				}else {
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
		            if (!rs.next()) {
		                // Eğer hiçbir sonuç bulunmazsa 404 Not Found dön
		                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		            }
					// DiseaseHistory nesnesini oluştur ve doldur
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
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
