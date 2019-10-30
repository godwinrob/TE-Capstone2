package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate dao;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.dao = new JdbcTemplate(dataSource);
	}

	private Campground mapToCampground(SqlRowSet results) {
		Campground c = new Campground();
		c.setParkId(results.getInt("park_id"));
		c.setId(results.getInt("campground_id"));
		c.setName(results.getString("name"));
		c.setOpenMonth(results.getString("open_from_mm"));
		c.setCloseMonth(results.getString("open_to_mm"));
		c.setDailyFee(results.getBigDecimal("daily_fee"));
		return c;
	}

	@Override
	public List<Campground> getAllCampgrounds(Park park) {
		List<Campground> foundCampgrounds = new ArrayList<Campground>();
		int parkId = park.getId();
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE park_id = ? ORDER BY name asc;";
		SqlRowSet results = dao.queryForRowSet(sql, parkId);
		while (results.next()) {
			Campground foundCampground = mapToCampground(results);
			foundCampgrounds.add(foundCampground);
		}
		return foundCampgrounds;
	}

	@Override
	public Campground getCampgroundById(int campgroundSelection) {
		Campground foundCampground = null;
		String sql = "SELECT campground_id, park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground WHERE campground_id = ? ORDER BY name asc;";
		SqlRowSet results = dao.queryForRowSet(sql, campgroundSelection);
		if (results.next()) {
			foundCampground = mapToCampground(results);
		}
		return foundCampground;
	}

}
