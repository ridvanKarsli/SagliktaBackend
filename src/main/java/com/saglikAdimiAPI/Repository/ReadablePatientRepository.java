package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.ReadablePatient;
import com.saglikAdimiAPI.Model.Patient;

import lombok.Getter;
import lombok.Setter;

@Repository
public class ReadablePatientRepository implements ReadablePatient {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
	private Connection conn;

	@Override
	public ResponseEntity<List<Patient>> getAllPatient(String token) {
		// TODO Auto-generated method stub
		List<Patient> doctorList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"User\" WHERE \"role\" = 'HASTA'";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				Patient patient = new Patient();
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
	public ResponseEntity<Patient> getPatient(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection(); // Veritabanı bağlantısını a
		Patient patient = new Patient();
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
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
