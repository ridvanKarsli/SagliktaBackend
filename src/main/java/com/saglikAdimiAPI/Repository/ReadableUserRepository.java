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

import com.saglikAdimiAPI.Abstraction.ReadablePerson;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class ReadableUserRepository implements ReadablePerson {

	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";
	private Connection conn;

	@Override
	public ResponseEntity<List<Person>> getAllPerson(String token) {
		List<Person> userList = new ArrayList<>(); // Kullanıcıları tutacak liste
		getConnection(); // Veritabanı bağlantısını al

		String query = "SELECT * FROM public.\"User\"";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();

			// Sonuçları döngü ile okuyoruz
			while (rs.next()) {
				Person user = new Person();
				user.setUserID(rs.getInt("userID"));
				user.setName(rs.getString("name").trim());
				user.setSurname(rs.getString("surname").trim());
				user.setRole(rs.getString("role").trim());
				user.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
				user.setEmail(rs.getString("email").trim());
				user.setPassword(rs.getString("password").trim());

				userList.add(user); // Listeye ekle
			}

			rs.close();
			ps.close();
			conn.close(); // Bağlantıyı kapat

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ResponseEntity.ok(userList);

	}
	

	@Override
	public ResponseEntity<Person> getLoggedPerson(String token) {
		// TODO Auto-generated method stub
		
		JwtService jwtService = new JwtService();
		
		Person personFromToken = jwtService.getPersonFromToken(token);
		

		getConnection(); // Veritabanı bağlantısını a
		Person person = new Person();		
		String query = "SELECT * FROM public.\"User\" WHERE \"userID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, personFromToken.getUserID());
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					person.setUserID(rs.getInt("userID"));
					person.setName(rs.getString("name").trim());
					person.setSurname(rs.getString("surname").trim());
					person.setRole(rs.getString("role").trim());
					person.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
					person.setEmail(rs.getString("email").trim());
					person.setPassword(rs.getString("password").trim());

				}else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(person);
	}
	
	@Override
	public ResponseEntity<Person> getPerson(int userID, String token) {
		// TODO Auto-generated method stub		

		getConnection(); // Veritabanı bağlantısını a
		Person person = new Person();		
		String query = "SELECT * FROM public.\"User\" WHERE \"userID\" = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setInt(1, userID);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) { // Verinin varlığını kontrol et
					person.setUserID(rs.getInt("userID"));
					person.setName(rs.getString("name").trim());
					person.setSurname(rs.getString("surname").trim());
					person.setRole(rs.getString("role").trim());
					person.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
					person.setEmail(rs.getString("email").trim());
					person.setPassword(rs.getString("password").trim());

				}else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
				conn.close();
				rs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Hata mesajını yazdır
		}

		return ResponseEntity.ok(person);
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
