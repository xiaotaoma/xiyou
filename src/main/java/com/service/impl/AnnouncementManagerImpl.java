package com.service.impl;

import com.dao.AnnouncementDao;
import com.main.BaseAction;
import com.model.Announcement;
import com.service.AnnouncementManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

public class AnnouncementManagerImpl implements AnnouncementManager {
	private AnnouncementDao announcementDao;
	
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Announcement announcement) throws SQLException {
		this.announcementDao.insert(announcement);
	}

	@Override
	public List<Announcement> getAnnouncements(int limit) throws SQLException {
		return this.announcementDao.getAnnouncements(limit);
	}

	public void setAnnouncementDao(AnnouncementDao announcementDao) {
		this.announcementDao = announcementDao;
	}

	public AnnouncementDao getAnnouncementDao() {
		return announcementDao;
	}

	public static void main(String[] args) throws SQLException {
		AnnouncementManager announcementManager = (AnnouncementManager) BaseAction.getIntance().getBean("announcementManager");
		announcementManager.getAnnouncements(1);
	}
}
