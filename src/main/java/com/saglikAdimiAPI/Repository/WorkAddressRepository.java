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

import com.saglikAdimiAPI.Abstraction.WorkAddressActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Address;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class WorkAddressRepository implements WorkAddressActionable {

	private static final String CONNECTION_STRING = "jdbc:postgresql://localhost:5433/SaglikAdimi_db?user=postgres&password=3519";
	private Connection conn;

	@Override
	public ResponseEntity<String> addWorkAddress(Address address, String token) {
		// TODO Auto-generated method stub
		JwtService jwtService = new JwtService();

		Person person = jwtService.getPersonFromToken(token);

		getConnection();
		PreparedStatement insert;

		String query = "INSERT INTO public.\"WorksAddress\"(\n"
				+ "	\"workPlaceName\", street, city, county, country, \"userID\")\n" + "	VALUES (?, ?, ?, ?, ?, ?);";

		try {
			insert = conn.prepareStatement(query);

			insert.setString(1, address.getWorkPlaceName().trim());
			insert.setString(2, address.getStreet().trim());
			insert.setString(3, address.getCity().trim());
			insert.setString(4, address.getCounty().trim());
			insert.setString(5, address.getCountry().trim());
			insert.setInt(6, person.getUserID());

			insert.executeUpdate();
			insert.close();
			conn.close();
			System.out.print("adres eklendi");
			return new ResponseEntity<>("Adres eklendi!", HttpStatus.OK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}

	}

	@Override
	public ResponseEntity<String> deleteWorkAddress(int addressID, String token) {
		// TODO Auto-generated method stub

		getConnection();

		// Silme sorgusu
		String query = "DELETE FROM public.\"WorksAddress\" WHERE \"adressID\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setInt(1, addressID);

			int rowsAffected = ps.executeUpdate();

			if (rowsAffected > 0) {
				System.out.println("adres silindi");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Adres silindi!", HttpStatus.OK);
			} else {
				System.out.println("adres yok");
				ps.close();
				conn.close();
				return new ResponseEntity<>("Adres bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<String> UpdateWorkAddress(Address address, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını oluştur

		String query = "UPDATE public.\"WorksAddress\"\n"
				+ "	SET  \"workPlaceName\"=?, street=?, city=?, county=?, country=? \n" + "	WHERE \"adressID\" = ?;";

		try (PreparedStatement insert = conn.prepareStatement(query)) {
			// document sütununu null olarak ayarlıyoruz
			insert.setString(1, address.getWorkPlaceName().trim());
			insert.setString(2, address.getStreet().trim());
			insert.setString(3, address.getCity().trim());
			insert.setString(4, address.getCounty().trim());
			insert.setString(5, address.getCountry().trim());
			insert.setInt(6, address.getAdressID());

			// Sorguyu çalıştır
			int rowsUpdated = insert.executeUpdate();
			if (rowsUpdated > 0) {
				System.out.println("adres güncellendi");
				conn.close();
				return new ResponseEntity<>("Adres güncellendi!", HttpStatus.OK);
			} else {
				System.out.println("Disease tablosunda güncellenecek satır bulunamadı.");
				conn.close();
				return new ResponseEntity<>("Adres bulunamadı!", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace(); // Hata durumunda log yazdır
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	@Override
	public ResponseEntity<List<Address>> getWorkAddresses(int userID, String token) {
		// TODO Auto-generated method stub

		getConnection();

		// SQL sorgusu
		String query = "SELECT * FROM public.\"WorksAddress\" WHERE \"userID\" = ?;";
		List<Address> addresses = new ArrayList<>();

		try (PreparedStatement stmt = conn.prepareStatement(query)) {
			// Sorguya parametreyi ata
			stmt.setInt(1, userID);

			// Sorguyu çalıştır
			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					// DiseaseHistory nesnesini oluştur ve doldur
					Address address = new Address();

					address.setAdressID(rs.getInt("adressID"));
					address.setWorkPlaceName(rs.getString("workPlaceName").trim());
					address.setStreet(rs.getString("street").trim());
					address.setCounty(rs.getString("county").trim());
					address.setCountry(rs.getString("country").trim());
					address.setCity(rs.getString("city").trim());
					address.setDoctorID(rs.getInt("userID"));

					// Listeye ekle
					addresses.add(address);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(addresses);

	}

	@Override
	public ResponseEntity<Address> getWorkAddress(int addressID, String token) {
		// TODO Auto-generated method stub
		getConnection(); // Veritabanı bağlantısını al
		Address address = new Address(); // Eğer sonuç bulunmazsa null dönecek

		String query = "SELECT * FROM public.\"WorksAddress\" WHERE \"adressID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, addressID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et

					address.setAdressID(rs.getInt("adressID"));
					address.setWorkPlaceName(rs.getString("workPlaceName").trim());
					address.setStreet(rs.getString("street").trim());
					address.setCounty(rs.getString("county").trim());
					address.setCountry(rs.getString("country").trim());
					address.setCity(rs.getString("city").trim());
					address.setDoctorID(rs.getInt("userID"));

				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(address);

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
