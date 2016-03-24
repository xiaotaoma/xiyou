package com.service.impl;

import com.dao.ReportDao;
import com.model.Report;
import com.service.ReportManager;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

public class ReportManagerImpl implements ReportManager {
	private ReportDao reportDao;
	@Transactional(rollbackFor=SQLException.class)
	@Override
	public void insert(Report report) throws SQLException {
		this.reportDao.insert(report);
	}

	@Override
	public List<Report> getReports(int hid) throws SQLException {
		return this.reportDao.getReports(hid);
	}

	public void setReportDao(ReportDao reportDao) {
		this.reportDao = reportDao;
	}

	public ReportDao getReportDao() {
		return reportDao;
	}
	
	@Override
	public List<Report> getBobedReports(int hid) throws SQLException {
		return this.reportDao.getBobedReports(hid);
	}
}
