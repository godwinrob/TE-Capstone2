package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Site;
import com.techelevator.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO{
	
	private JdbcTemplate dao;
	
	public JDBCSiteDAO(DataSource dataSource) {
		this.dao = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Site> getAllSites(Campground campground) {
		List<Site> returnedSites = new ArrayList<Site>();
		String sql = "SELECT site_id, campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities FROM site WHERE campgound = ?;";
		SqlRowSet results = dao.queryForRowSet(sql, campground);
		while (results.next()) {
			Site foundSite = mapToSite(results);
			returnedSites.add(foundSite);
			}
		return returnedSites;
	}

	private Site mapToSite(SqlRowSet results) {
		Site s = new Site();
		s.setSiteId(results.getInt("site_id"));
		s.setCampgroundId(results.getInt("campground_id"));
		s.setSiteNumber(results.getInt("site_number"));
		s.setMaxOccupancy(results.getInt("max_occupancy"));
		s.setAccessible(results.getBoolean("accessible"));
		s.setMaxRvLength(results.getInt("max_rv_length"));
		s.setUtilities(results.getBoolean("utilities"));
		return s;
	}
	
	

}
