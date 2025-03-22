package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
		if (!isAddressUsable(address)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
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
		if (!isAddressUsable(address)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
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
	
	public Boolean isAddressUsable(Address address) {
	    // City boş olamaz ve 150 karakteri geçemez
	    if (address.getCity() == null || address.getCity().trim().isEmpty() || address.getCity().length() > 150) {
	        return false;
	    }

	    // Country boş olamaz ve 150 karakteri geçemez
	    if (address.getCountry() == null || address.getCountry().trim().isEmpty() || address.getCountry().length() > 150) {
	        return false;
	    }

	    // County boş olamaz ve 150 karakteri geçemez
	    if (address.getCounty() == null || address.getCounty().trim().isEmpty() || address.getCounty().length() > 150) {
	        return false;
	    }

	    // Street boş olamaz ve 150 karakteri geçemez
	    if (address.getStreet() == null || address.getStreet().trim().isEmpty() || address.getStreet().length() > 150) {
	        return false;
	    }

	    // WorkPlaceName boş olamaz ve 150 karakteri geçemez
	    if (address.getWorkPlaceName() == null || address.getWorkPlaceName().trim().isEmpty() || address.getWorkPlaceName().length() > 150) {
	        return false;
	    }

	    // Tüm alanlar uygun olduğunda true döndür
	    return true;
	}



}
