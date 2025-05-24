package com.saglikAdimiAPI.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.saglikAdimiAPI.Abstraction.AnnouncementActionable;
import com.saglikAdimiAPI.Model.Announcement;
import com.saglikAdimiAPI.Repository.AnnouncementRepository;

@Service
public class AnnouncementService implements AnnouncementActionable {

	private final AnnouncementRepository announcementRepository;

	@Autowired
	public AnnouncementService(AnnouncementRepository announcementRepository) {
		this.announcementRepository = announcementRepository;
	}

	@Override
	public ResponseEntity<String> addAnnouncement(Announcement announcementRequest, String token) {
		// TODO Auto-generated method stub
		if (!isAnnouncementsUsable(announcementRequest)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Lütfen Bilgileri Kontrol Edip Tekrardan Deneyin!");
		}
		return announcementRepository.addAnnouncement(announcementRequest, token);
	}

	@Override
	public ResponseEntity<String> deleteAnnouncement(int announcementID, String token) {
		// TODO Auto-generated method stub
		return announcementRepository.deleteAnnouncement(announcementID, token);
	}

	@Override
	public ResponseEntity<Announcement> getAnnouncement(int announcementID, String token) {
		// TODO Auto-generated method stub
		return announcementRepository.getAnnouncement(announcementID, token);
	}

	@Override
	public ResponseEntity<List<Announcement>> getAnnouncements(int userID, String token) {
		// TODO Auto-generated method stub
		return announcementRepository.getAnnouncements(userID, token);
	}

	private Boolean isAnnouncementsUsable(Announcement announcement) {
		String title = announcement.getTitle();
		String content = announcement.getContent();

		if (title == null || title.isEmpty() || title.length() > 100) {
			return false;
		}

		if (content == null || content.isEmpty() || content.length() > 1000) {
			return false;
		}

		return true;
	}

}
