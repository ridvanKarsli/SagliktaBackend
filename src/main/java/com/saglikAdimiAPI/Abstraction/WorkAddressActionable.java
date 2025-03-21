package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Address;

public interface WorkAddressActionable {

	ResponseEntity<String> addWorkAddress(Address address, String token);

	ResponseEntity<String> deleteWorkAddress(int addressID, String token);

	ResponseEntity<String> UpdateWorkAddress(Address address, String token);

	ResponseEntity<List<Address>> getWorkAddresses(int userID, String token);

	ResponseEntity<Address> getWorkAddress(int addressID, String token);

}
