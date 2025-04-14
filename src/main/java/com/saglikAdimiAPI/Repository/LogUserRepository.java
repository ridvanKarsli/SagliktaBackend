package com.saglikAdimiAPI.Repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.saglikAdimiAPI.Abstraction.Logable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.PasswordUtil;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Model.Person;

@Repository
public class LogUserRepository implements Logable<Patient> {
	
	private static final String CONNECTION_STRING = "jdbc:postgresql://clhtb6lu92mj2.cluster-czz5s0kz4scl.eu-west-1.rds.amazonaws.com:5432/d3ee0thpk00tbe?user=ubuffdepf41jfs&password=p22f739ec6892fed407dc52ed86c1963b0d0053957d30928da2bfd0d24bff391e";

	private Connection conn;
	PasswordUtil passwordUtil = new PasswordUtil();

	// veri tabanı işlemleri
	@Override
	public ResponseEntity<String> login(Person person) {		
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

			String email = person.getEmail().trim();
			String password = person.getPassword().trim();
			getConnection();

			String query = "SELECT * FROM public.\"User\" WHERE \"email\" = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			rs = ps.executeQuery();

			if (rs.next()) {
				String storedPassword = rs.getString("password").trim();

				if (PasswordUtil.verifyPassword(password, storedPassword)) {
					Person newPerson = getPersonWithEmail(person.getEmail());
					JwtService jwtService = new JwtService();
					String token = jwtService.generateToken(newPerson);

					return new ResponseEntity<>(token, HttpStatus.OK);
				} else {
					System.out.println("Şifre yanlış!");
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hatalı şifre");

				}
			} else {
				System.out.println("Kullanıcı bulunamadı!");
				return new ResponseEntity<>("Kullanıcı bulunamadı", HttpStatus.NOT_FOUND);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Veritabanı hatası: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (ps != null)
					ps.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Veritabanı hatası: " + e.getMessage());
			}
		}
	}

	@Override
	public ResponseEntity<String> SignUp(Person person) {
		String name = person.getName().trim();
		String surname = person.getSurname().trim();
		String role = person.getRole().trim();
		LocalDate dateOfBirth = person.getDateOfBirth();
		String email = person.getEmail().trim();
		String password = PasswordUtil.hashPassword(person.getPassword().trim());

		getConnection();
		PreparedStatement insert;

		try {
			String query = "INSERT INTO public.\"User\"(\n"
					+ " name, surname, role, email, password, \"dateOfBirth\")\n" + " VALUES (?, ?, ?, ?, ?, ?);";

			insert = conn.prepareStatement(query);
			insert.setString(1, name);
			insert.setString(2, surname);
			insert.setString(3, role);
			insert.setString(4, email);
			insert.setString(5, password);
			insert.setDate(6, Date.valueOf(dateOfBirth));

			insert.executeUpdate();
			insert.close();
			conn.close();
			System.out.print("Kullanıcı veri tabanına kaydedildi");
			return new ResponseEntity<>("Kayıt başarılı!", HttpStatus.OK);
		} catch (SQLException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Veritabanı hatası: " + e.getMessage());
		}
	}

	public Person getPersonWithEmail(String email) {
		Person person = null;
		getConnection();

		String query = "SELECT * FROM public.\"User\" WHERE \"email\" = ?";

		try {
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, email.trim());
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				person = new Person();
				person.setUserID(rs.getInt("userID"));
				person.setName(rs.getString("name").trim());
				person.setSurname(rs.getString("surname").trim());
				person.setRole(rs.getString("role"));
				person.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
				person.setEmail(rs.getString("email"));
				person.setPassword(rs.getString("password"));
			}

			rs.close();
			ps.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return person;
	}

	public boolean emailUsable(String email) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		getConnection();

		try {

			String query = "SELECT * FROM public.\"User\" WHERE \"email\" = ?";
			ps = conn.prepareStatement(query);
			ps.setString(1, email);
			rs = ps.executeQuery();

			if (rs.next()) {
				return false; // Eğer sonuç varsa, yani e-posta zaten varsa, kullanılabilir değil
			} else {
				return true; // Eğer sonuç yoksa, e-posta kullanılabilir
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false; // Hata durumunda e-posta kullanılabilir olarak kabul etmiyoruz
		} finally {
			try {
				if (rs != null) {
					rs.close(); // ResultSet'i kapatıyoruz
				}
				if (ps != null) {
					ps.close(); // PreparedStatement'i kapatıyoruz
				}
				if (conn != null) {
					conn.close(); // Bağlantıyı kapatıyoruz
				}
			} catch (SQLException e) {
				e.printStackTrace(); // Eğer kapanırken hata olursa, onu da logluyoruz
			}
		}
	}


	/*

	@Override
	public ResponseEntity<String> verificationEmail(String email, String code) {
		// TODO Auto-generated method stub
		EmailService emailService = new EmailService();
		if (emailService.verifyCode(email, code)) {
			 return ResponseEntity.ok("Doğrulama işlemi başarıyla gerçekleştirildi!");
        }
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doğrulama Kodu hatalı!");
	}

	@Override
	public ResponseEntity<String> sendVerificationCode(String email) {
		// TODO Auto-generated method stub
		EmailService emailService = new EmailService();
		if(emailService.sendVerificationCode(email)) {
			return ResponseEntity.ok("Doğrulama kodu başarıyla gönderildi!");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Doğrulama kodu gönderilemedi!");
	}
	*/

	private void getConnection() {
		try {
			conn = DriverManager.getConnection(CONNECTION_STRING);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
