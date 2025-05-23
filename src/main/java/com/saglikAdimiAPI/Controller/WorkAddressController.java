package com.saglikAdimiAPI.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saglikAdimiAPI.Abstraction.WorkAddressActionable;
import com.saglikAdimiAPI.Helper.JwtService;
import com.saglikAdimiAPI.Model.Address;
import com.saglikAdimiAPI.Service.WorkAddressService;

@RestController
@RequestMapping("/workAddress")
public class WorkAddressController implements WorkAddressActionable {

	private final WorkAddressService workAddressService;

	private final JwtService jwtService;

	@Autowired
	public WorkAddressController(WorkAddressService workAddressService, JwtService jwtService) {
		this.workAddressService = workAddressService;
		this.jwtService = jwtService;
	}

	@PostMapping("/addWorkAddress")
	@Override
	public ResponseEntity<String> addWorkAddress(@RequestBody Address address,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return workAddressService.addWorkAddress(address, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@DeleteMapping("/deleteWorkAddress")
	@Override
	public ResponseEntity<String> deleteWorkAddress(@RequestParam int addressID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return workAddressService.deleteWorkAddress(addressID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@PutMapping("/updateWorkAddress")
	@Override
	public ResponseEntity<String> UpdateWorkAddress(@RequestBody Address address,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return workAddressService.UpdateWorkAddress(address, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired");
		}
	}

	@GetMapping("/getWorkAddreses")
	@Override
	public ResponseEntity<List<Address>> getWorkAddresses(@RequestParam int userID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return workAddressService.getWorkAddresses(userID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/getWorkAddress")
	@Override
	public ResponseEntity<Address> getWorkAddress(@RequestParam int addressID,
			@RequestHeader("Authorization") String token) {
		// TODO Auto-generated method stub

		if (!jwtService.isTokenExpired(token)) {
			if (jwtService.getPersonFromToken(token).getRole().equals("DOKTOR")) {
				return workAddressService.getWorkAddress(addressID, token);
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
			}

		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
