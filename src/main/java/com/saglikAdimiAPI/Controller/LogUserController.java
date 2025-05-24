package com.saglikAdimiAPI.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.Logable;
import com.saglikAdimiAPI.Helper.EmailService;
import com.saglikAdimiAPI.Model.PublicUser;
import com.saglikAdimiAPI.Model.Person;
import com.saglikAdimiAPI.Service.LogUserService;

@RestController
@RequestMapping("/logUser")
public class LogUserController implements Logable<PublicUser> {

	private final LogUserService logUserService;

	@Autowired
	public LogUserController(LogUserService logUserService) {
		this.logUserService = logUserService;
	}

	@PostMapping("/logingUser")
	@Override
	public ResponseEntity<String> login(@RequestBody Person person) {

		return logUserService.login(person);
	}

	@PostMapping("/signupUser")
	@Override
	public ResponseEntity<String> SignUp(@RequestBody Person person) {
		EmailService emailService = new EmailService();
		boolean isValid = emailService.isEmailValid(person.getEmail());
		if (isValid) {
			emailService.sendWelcomeMail(person.getEmail());
			return logUserService.SignUp(person);
		} else {
			return new ResponseEntity<>("Geçersiz e-posta adresi!", HttpStatus.BAD_REQUEST);
		}

	}

}
