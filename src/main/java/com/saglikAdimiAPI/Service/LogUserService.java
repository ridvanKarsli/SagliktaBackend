package com.saglikAdimiAPI.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.Logable;
import com.saglikAdimiAPI.Model.Patient;
import com.saglikAdimiAPI.Model.Person;
import com.saglikAdimiAPI.Repository.LogUserRepository;

@Service
public class LogUserService implements Logable<Patient> {

	private final LogUserRepository logUserRepository;

	@Autowired
	public LogUserService(LogUserRepository logUserRepository) {
		this.logUserRepository = logUserRepository;
	}

	@Override
	public ResponseEntity<String> login(Person person) {
		// TODO Auto-generated method stub
		if ((person.getEmail().isEmpty()) || (person.getPassword().isEmpty())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Kullanıcı adı veya şifre boş olamaz");

		}
		return logUserRepository.login(person);
	}

	@Override
	public ResponseEntity<String> SignUp(Person person) {
		// TODO Auto-generated method stub
		if (person.getEmail() == null || person.getEmail().isEmpty() || person.getName() == null
				|| person.getName().isEmpty() || person.getSurname() == null || person.getSurname().isEmpty()
				|| person.getRole() == null || person.getRole().isEmpty() || person.getDateOfBirth() == null
				|| person.getPassword() == null || person.getPassword().isEmpty()) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen gerekli bilgileri doldurunuz");
		}

		return logUserRepository.SignUp(person);
	}

	@Override
	public ResponseEntity<String> refreshToken(String token) {
		// TODO Auto-generated method stub
		return logUserRepository.refreshToken(token);
	}

	@Override
	public ResponseEntity<String> verificationEmail(String email, String code) {
		// TODO Auto-generated method stub
		return logUserRepository.verificationEmail(email, code);
	}

	@Override
	public ResponseEntity<String> sendVerificationCode(String email) {
		// TODO Auto-generated method stub
		return logUserRepository.sendVerificationCode(email);
	}

}
