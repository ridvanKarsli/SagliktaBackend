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

import com.saglikAdimiAPI.Abstraction.SpecializationActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Person;
import com.saglikAdimiAPI.Model.Specialization;

@Repository
public class SpecializationRepository implements SpecializationActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
	private Connection conn;

	@Override
	public ResponseEntity<String> addSpecialization(Specialization specialization, String token) {
		// TODO Auto-generated method stub
		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"Specialization\"(\n"
				+ "	\"nameOfSpecialization\", \"specializationExperience\", \"userID\")\n" + "	VALUES (?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, specialization.getNameOfSpecialization().trim());
			insert.setInt(2, specialization.getSpecializationExperience());
			insert.setInt(3, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();
			System.out.print("uzmanlık alanı eklendi");
			return new ResponseEntity<>("Uzmanlık eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> deleteSpecialization(int specializationID, String token) {
		// TODO Auto-generated method stub
		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"Specialization\" WHERE \"specializationID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, specializationID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("uzmanlık alanı silindi");

				ps.close();
				conn.close();
				return new ResponseEntity<>("Uzmanlık silindi!", HttpStatus.OK);
			} else {
				System.out.println("uzmanlık alanı bulunamadı");

				ps.close();
				conn.close();
				return new ResponseEntity<>("Uzmanlık bulunamdı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> updateSpecialization(Specialization specialization, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını oluştur

		String query = "UPDATE public.\"Specialization\"\n"
				+ "	SET  \"nameOfSpecialization\"=?, \"specializationExperience\"=?\n"
				+ "	WHERE \"specializationID\" = ?;";

		try (PreparedStatement insert = conn.prepareStatement(query)) {
			// document sütununu null olarak ayarlıyoruz
			insert.setString(1, specialization.getNameOfSpecialization().trim());
			insert.setInt(2, specialization.getSpecializationExperience());
			insert.setInt(3, specialization.getSpecializationID());

			// Sorguyu çalıştır
			int rowsUpdated = insert.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Uzamnlık alanı güncellendi");
				conn.close();
				return new ResponseEntity<>("Uzmanlık güncellendi!", HttpStatus.OK);
			} else {
				System.out.println("uzamlık alanı bulunamdı");
				conn.close();
				return new ResponseEntity<>("Uzmanlık bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Hata durumunda log yazdır
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Specialization>> getSpecializations(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection();

		// SQL sorgusu
		String query = "SELECT * FROM public.\"Specialization\" WHERE \"userID\" = ?;";
		List<Specialization> specializationList = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Sorguya parametreyi ata
			stmt.setInt(1, userID);

			// Sorguyu çalıştır
			try (ResultSet rs = stmt.executeQuery()) {
	            if (!rs.next()) {
	                // Eğer hiçbir sonuç bulunmazsa 404 Not Found dön
	                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
	            }
				while (rs.next()) {
					// DiseaseHistory nesnesini oluştur ve doldur
					Specialization specialization = new Specialization();

					specialization.setSpecializationID(rs.getInt("specializationID"));
					specialization.setNameOfSpecialization(rs.getString("nameOfSpecialization").trim());
					specialization.setSpecializationExperience(rs.getInt("specializationExperience"));
					specialization.setDoctorID(rs.getInt("userID"));

					// Listeye ekle
					specializationList.add(specialization);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
		return ResponseEntity.ok(specializationList);

	}

	@Override
	public ResponseEntity<Specialization> getSpecialization(int specializationID, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını al
		Specialization specialization = new Specialization(); // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"Specialization\" WHERE \"specializationID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, specializationID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et

					specialization.setSpecializationID(rs.getInt("specializationID"));
					specialization.setNameOfSpecialization(rs.getString("nameOfSpecialization").trim());
					specialization.setSpecializationExperience(rs.getInt("specializationExperience"));
					specialization.setDoctorID(rs.getInt("userID"));

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

		return ResponseEntity.ok(specialization);

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
