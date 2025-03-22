package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.DiseaseActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Disease;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class DiseaseRepository implements DiseaseActionable<Patient> {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
	private Connection conn;

	@Override
	public ResponseEntity<String> addDisease(Disease disease, String token) {
		// TODO Auto-generated method stub

		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);
		System.out.print(person.getUserID());

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"Diseases\"( name, \"dateOfDiagnosis\", \"userID\") VALUES (?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, disease.getDiseaseName().trim());
			insert.setDate(2, Date.valueOf(disease.getDateOfDiagnosis()));
			insert.setInt(3, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();
			System.out.print("1 hastalık eklendi");
			return new ResponseEntity<>("Hastalık eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Disease>> getDiseases(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection();

		// SQL sorgusu
		String query = "SELECT * FROM public.\"Diseases\" WHERE \"userID\" = ?;";
		List<Disease> diseases = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Sorguya parametreyi ata
			stmt.setInt(1, userID);

			// Sorguyu çalıştır
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// DiseaseHistory nesnesini oluştur ve doldur
					Disease disease = new Disease();

					disease.setDiseaseID(rs.getInt("diseaseID"));
					disease.setDiseaseName(rs.getString("name").trim());
					disease.setDateOfDiagnosis(rs.getDate("dateOfDiagnosis").toLocalDate());
					disease.setUserID(rs.getInt("userID"));

					// Listeye ekle
					diseases.add(disease);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(diseases);
	}

	@Override
	public ResponseEntity<String> deleteDisease(int diseaseID, String token) {
		// TODO Auto-generated method stub

		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"Diseases\" WHERE \"diseaseID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, diseaseID); // diseaseID'yi sorguya yerleştir

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("disease tablosundan diseaseID = " + diseaseID + " olan satır silindi.");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Hastalık silindi!", HttpStatus.OK);
			} else {
				System.out.println("disease tablosunda diseaseID = " + diseaseID + " olan bir kayıt bulunamadı.");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Hastalık bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> updateDisease(Disease disease, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını oluştur

		String query = "UPDATE public.\"Diseases\" " + "SET \"name\" = ?, " + "\"dateOfDiagnosis\" = ? "
				+ "WHERE \"diseaseID\" = ?;";

		try (PreparedStatement insert = conn.prepareStatement(query)) {
			// document sütununu null olarak ayarlıyoruz
			insert.setString(1, disease.getDiseaseName().trim());
			insert.setDate(2, Date.valueOf(disease.getDateOfDiagnosis()));
			insert.setInt(3, disease.getDiseaseID());

			// Sorguyu çalıştır
			int rowsUpdated = insert.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("Disease tablosu başarıyla güncellendi.");
				conn.close();
				return new ResponseEntity<>("Hastalık Güncellendi!", HttpStatus.OK);
			} else {
				System.out.println("Disease tablosunda güncellenecek satır bulunamadı.");
				conn.close();
				return new ResponseEntity<>("Hastalık bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Hata durumunda log yazdır
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<Disease> getDisease(int diseaseID, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını al
		Disease disease = new Disease(); // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"Diseases\" WHERE \"diseaseID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, diseaseID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et

					disease.setDiseaseID(diseaseID);
					disease.setDiseaseName(rs.getString("name").trim());
					disease.setDateOfDiagnosis(rs.getDate("dateOfDiagnosis").toLocalDate());
					disease.setUserID(rs.getInt("userID"));

				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(disease);
	}

	private void getConnection() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public ResponseEntity<List<String>> getDiseaseNames(String token) {
		// TODO Auto-generated method stub

		getConnection(); // Veritabanı bağlantısını a
		Patient patient = new Patient();
		String query = "SELECT * FROM public.\"DiseaseNames\"";
		List<String> diseaseNameList = new ArrayList<>();

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) { // Verinin varlığını kontrol et

					diseaseNameList.add(rs.getString("diseaseName").trim()); // Boşlukları temizle

				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(diseaseNameList);
	}

}
