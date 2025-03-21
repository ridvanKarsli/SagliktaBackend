package com.saglikAdimiAPI.Abstraction;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.saglikAdimiAPI.Model.Announcement;

public interface AnnouncementActionable {

	ResponseEntity<String> addAnnouncement(Announcement announcement, String token);

	ResponseEntity<String> deleteAnnouncement(int announcementID, String token);

	ResponseEntity<Announcement> getAnnouncement(int announcementID, String token);

	ResponseEntity<List<Announcement>> getAnnouncements(int userID, String token);

}
