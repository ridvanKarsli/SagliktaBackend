package com.saglikAdimiAPI.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
		if (!isPersonUsable(person)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		

		return logUserRepository.SignUp(person);
	}

	/*
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
	*/
	
	public boolean isPersonUsable(Person person) {
	    // İsim ve soyisim sadece harflerden oluşmalı ve 20 karakteri geçmemeli
	    if (person.getName() == null || person.getName().length() > 20) {
	        return false;
	    }

	    if (person.getSurname() == null ||  person.getSurname().length() > 20) {
	        return false;
	    }

	    // Role "DOKTOR" veya "HASTA" olmalı
	    if (person.getRole() == null || (!person.getRole().equals("DOKTOR") && !person.getRole().equals("HASTA"))) {
	        return false;
	    }

	    // Password en az 8 karakter olmalı
	    if (person.getPassword() == null || person.getPassword().length() < 8 || person.getPassword().length() > 20) {
	        return false;
	    }

	    // DateOfBirth "yyyy-MM-dd" formatında olmalı
	    if (person.getDateOfBirth() == null) {
	        return false;
	    }

	    // DateOfBirth kontrolü
	    LocalDate birthDate = person.getDateOfBirth();
	    
	    // Minimum 90 yıl önceki tarih ve maksimum 90 yıl sonraki tarih
	    LocalDate minimumDate = LocalDate.now().minusYears(90); // 90 yıl önce
	    LocalDate maximumDate = LocalDate.now().plusYears(90); // 90 yıl sonrası

	    // DateOfBirth'un geçerli olup olmadığını ve aradaki tarih aralığına uygun olup olmadığını kontrol et
	    if (birthDate.isBefore(minimumDate) || birthDate.isAfter(maximumDate)) {
	        return false;
	    }

	    return true; // Tüm kontroller geçtiyse
	}

}
