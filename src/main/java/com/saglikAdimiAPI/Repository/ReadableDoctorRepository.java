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

import com.saglikAdimiAPI.Abstraction.ReadableDoctor;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Doctor;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class ReadableDoctorRepository implements ReadableDoctor {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	private Connection conn;

	@Override
	public ResponseEntity<List<Doctor>> getAllDoctor(String token) {
		// TODO Auto-generated method stub
		List<Doctor> doctorList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"User\" WHERE \"role\" = 'doctor'";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				Doctor doctor = new Doctor();
				doctor.setUserID(rs.getInt("userID"));
				doctor.setName(rs.getString("name").trim());
				doctor.setSurname(rs.getString("surname").trim());
				doctor.setRole(rs.getString("role").trim());
				doctor.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
				doctor.setEmail(rs.getString("email").trim());
				doctor.setPassword(rs.getString("password").trim());

				int userID = rs.getInt("userID");

				SpecializationRepository ss = new SpecializationRepository();
				doctor.setSpecialization(ss.getSpecializations(userID, token).getBody());

				WorkAddressRepository ws = new WorkAddressRepository();
				doctor.setWorksAddress(ws.getWorkAddresses(userID, token).getBody());

				ContactInfoRespository cs = new ContactInfoRespository();
				doctor.setContactInfor(cs.getAllContect(userID, token).getBody());

				AnnouncementRepository as = new AnnouncementRepository();
				doctor.setAnnouncement(as.getAnnouncements(userID, token).getBody());
				doctorList.add(doctor); // Listeye ekle
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
	public ResponseEntity<Doctor> getDoctor(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection(); // Veritabanı bağlantısını al
		Doctor doctor = null; // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"User\" WHERE \"userID\" = ? AND \"role\" = 'doctor'";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, userID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					doctor = new Doctor();
					doctor.setUserID(rs.getInt("userID"));
					doctor.setName(rs.getString("name").trim());
					doctor.setSurname(rs.getString("surname").trim());
					doctor.setRole(rs.getString("role").trim());
					doctor.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
					doctor.setEmail(rs.getString("email").trim());
					doctor.setPassword(rs.getString("password").trim());

					SpecializationRepository ss = new SpecializationRepository();
					doctor.setSpecialization(ss.getSpecializations(userID, token).getBody());

					WorkAddressRepository ws = new WorkAddressRepository();
					doctor.setWorksAddress(ws.getWorkAddresses(userID, token).getBody());

					ContactInfoRespository cs = new ContactInfoRespository();
					doctor.setContactInfor(cs.getAllContect(userID, token).getBody());

					AnnouncementRepository as = new AnnouncementRepository();
					doctor.setAnnouncement(as.getAnnouncements(userID, token).getBody());

				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(doctor);

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
