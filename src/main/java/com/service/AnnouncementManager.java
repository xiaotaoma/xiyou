package com.service;

import com.model.Announcement;

import java.sql.SQLException;
import java.util.List;

public interface AnnouncementManager {
	public void insert(Announcement announcement) throws SQLException;

	public List<Announcement> getAnnouncements(int limit) throws SQLException;
}
