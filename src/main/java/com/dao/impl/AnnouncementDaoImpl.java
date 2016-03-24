package com.dao.impl;

import com.dao.AnnouncementDao;
import com.ibatis.sqlmap.client.SqlMapClient;
import com.model.Announcement;

import java.sql.SQLException;
import java.util.List;

public class AnnouncementDaoImpl implements AnnouncementDao {
	private SqlMapClient sqlMapClient;
	@Override
	public void insert(Announcement announcement) throws SQLException {
		this.sqlMapClient.insert("Announcement.insert", announcement);
	}

	@Override
	public List<Announcement> getAnnouncements(int limit) throws SQLException {
		return this.sqlMapClient.queryForList("Announcement.getAnnouncement", limit);
	}

	public void setSqlMapClient(SqlMapClient sqlMapClient) {
		this.sqlMapClient = sqlMapClient;
	}

	public SqlMapClient getSqlMapClient() {
		return sqlMapClient;
	}

}
