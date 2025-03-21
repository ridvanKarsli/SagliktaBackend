package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.WorkAddressActionable;
import com.saglikAdimiAPI.Model.Address;
import com.saglikAdimiAPI.Repository.WorkAddressRepository;

@Service
public class WorkAddressService implements WorkAddressActionable {

	private final WorkAddressRepository workAddressRepository;

	@Autowired
	public WorkAddressService(WorkAddressRepository workAddressRepository) {
		this.workAddressRepository = workAddressRepository;
	}

	@Override
	public ResponseEntity<String> addWorkAddress(Address address, String token) {
		// TODO Auto-generated method stub
		return workAddressRepository.addWorkAddress(address, token);
	}

	@Override
	public ResponseEntity<String> deleteWorkAddress(int addressID, String token) {
		// TODO Auto-generated method stub
		return workAddressRepository.deleteWorkAddress(addressID, token);
	}

	@Override
	public ResponseEntity<String> UpdateWorkAddress(Address address, String token) {
		// TODO Auto-generated method stub
		return workAddressRepository.UpdateWorkAddress(address, token);
	}

	@Override
	public ResponseEntity<List<Address>> getWorkAddresses(int userID, String token) {
		// TODO Auto-generated method stub
		return workAddressRepository.getWorkAddresses(userID, token);
	}

	@Override
	public ResponseEntity<Address> getWorkAddress(int addressID, String token) {
		// TODO Auto-generated method stub
		return workAddressRepository.getWorkAddress(addressID, token);
	}

}
