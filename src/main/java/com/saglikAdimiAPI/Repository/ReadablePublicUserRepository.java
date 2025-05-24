package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.ReadablePublicUser;
import com.saglikAdimiAPI.Model.PublicUser;

@Repository
public class ReadablePublicUserRepository implements ReadablePublicUser {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	private Connection conn;

	@Override
	public ResponseEntity<List<PublicUser>> getAllPublicUser(String token) {
		// TODO Auto-generated method stub
		List<PublicUser> doctorList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"User\" WHERE \"role\" = 'HASTA'";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				PublicUser patient = new PublicUser();
				patient.setUserID(rs.getInt("userID"));
				patient.setName(rs.getString("name").trim());
				patient.setSurname(rs.getString("surname").trim());
				patient.setRole(rs.getString("role").trim());
				patient.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
				patient.setEmail(rs.getString("email").trim());
				patient.setPassword(rs.getString("password").trim());

				DiseaseRepository dr = new DiseaseRepository();
				patient.setDiseases(dr.getDiseases(rs.getInt("userID"), token).getBody());

				doctorList.add(patient);
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(doctorList);

	}

	@Override
	public ResponseEntity<PublicUser> getPublicUser(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection(); // Veritabanı bağlantısını a
		PublicUser patient = new PublicUser();
		String query = "SELECT * FROM public.\"User\" WHERE \"userID\" = ? AND \"role\" = 'HASTA'";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, userID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					patient.setUserID(rs.getInt("userID"));
					patient.setName(rs.getString("name").trim());
					patient.setSurname(rs.getString("surname").trim());
					patient.setRole(rs.getString("role").trim());
					patient.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
					patient.setEmail(rs.getString("email").trim());
					patient.setPassword(rs.getString("password").trim());

					DiseaseRepository dr = new DiseaseRepository();
					patient.setDiseases(dr.getDiseases(rs.getInt("userID"), token).getBody());

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(patient);

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
