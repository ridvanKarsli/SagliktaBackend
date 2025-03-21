package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.ContactInfoActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.ContactInfo;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class ContactInfoRespository implements ContactInfoActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5433/SaglikAdimi_db?user=postgres&password=3519";
	private Connection conn;

	@Override
	public ResponseEntity<String> addContactInfo(ContactInfo contactInfo, String token) {
		// TODO Auto-generated method stub
		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);
		System.out.print(person.getUserID());

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"ContactInfo\"(\n" + " email, \"phoneNumber\", \"userID\")\n"
				+ "	VALUES (?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, contactInfo.getEmail().trim());
			insert.setString(2, contactInfo.getPhoneNumber().trim());
			insert.setInt(3, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();

			return new ResponseEntity<>("Adres eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());

		}
	}

	@Override
	public ResponseEntity<String> updateContactInfo(ContactInfo contactInfo, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını oluştur

		String query = "UPDATE public.\"ContactInfo\"\n" + "	SET email=?, \"phoneNumber\"=? \n"
				+ "	WHERE \"contactID\"=?;";

		try (PreparedStatement insert = conn.prepareStatement(query)) {
			// document sütununu null olarak ayarlıyoruz

			insert.setString(1, contactInfo.getEmail().trim());
			insert.setString(2, contactInfo.getPhoneNumber().trim());
			insert.setInt(3, contactInfo.getContactID());

			// Sorguyu çalıştır
			int rowsUpdated = insert.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("iletişim bilgisi güncellendi.");
				conn.close();
				return new ResponseEntity<>("Iletişim bilgisi güncellendi!", HttpStatus.OK);
			} else {
				System.out.println("Iletişim tablosunda güncellenecek satır bulunamadı.");
				conn.close();
				return new ResponseEntity<>("Iletişim bilgisi bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Hata durumunda log yazdır
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<ContactInfo> getContect(int ContactID, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını al
		ContactInfo contactInfo = null; // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"ContactInfo\" WHERE \"contactID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, ContactID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					contactInfo = new ContactInfo();

					contactInfo.setContactID(ContactID);
					contactInfo.setEmail(rs.getString("email").trim());
					contactInfo.setPhoneNumber(rs.getString("phoneNumber").trim());

				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(contactInfo);
	}

	@Override
	public ResponseEntity<List<ContactInfo>> getAllContect(int userID, String token) {
		// TODO Auto-generated method stub

		List<ContactInfo> contactList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"ContactInfo\" WHERE \"userID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);

			ps.setInt(1, userID);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				ContactInfo contact = new ContactInfo();
				contact.setContactID(rs.getInt("contactID"));
				contact.setEmail(rs.getString("email").trim());
				contact.setPhoneNumber(rs.getString("phoneNumber").trim());
				contact.setDoctorID(rs.getInt("userID"));

				contactList.add(contact); // Listeye ekle
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(contactList);

	}

	@Override
	public ResponseEntity<String> deleteContactInfo(int contactID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"ContactInfo\" WHERE \"contactID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, contactID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("iletişim bilgisi silindi");
				return new ResponseEntity<>("iletişim bilgisi silindi!", HttpStatus.OK);
			} else {
				System.out.println("iletişim bilgisi bulunamadı");
				ps.close();
				conn.close();
				return new ResponseEntity<>("iletişim bilgisi bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
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
