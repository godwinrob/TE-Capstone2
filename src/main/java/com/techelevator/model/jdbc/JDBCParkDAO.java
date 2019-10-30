package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {
	
	private JdbcTemplate dao;
	
	public JDBCParkDAO(DataSource dataSource) {
		this.dao = new JdbcTemplate(dataSource);
	}
	private Park mapToPark(SqlRowSet results) {
		Park p = new Park ();		
		p.setId(results.getInt("park_id"));
		p.setName(results.getString("name"));
		p.setLocation(results.getString("location"));
		p.setEstDate(results.getDate("establish_date"));
		p.setArea(results.getInt("area"));
		p.setAnnualVisitorCount(results.getInt("visitors"));
		p.setDescription(results.getString("description"));
		return p;
	}


	@Override
	public List<Park> getAllParks() {
		List <Park> foundParks = new ArrayList<Park> ();
		String sql = "SELECT park_id, name, location, establish_date, area, visitors, description FROM park ORDER BY name asc;";
		SqlRowSet results = dao.queryForRowSet(sql);
		while (results.next()) {
			Park foundPark = mapToPark(results);
			foundParks.add(foundPark);
			}
		return foundParks;
	}	

	@Override
	public List<Park> searchParksByName(String userNameSearch) { // pass in user nameSearch
		List <Park> foundParks = new ArrayList<Park> ();
		String fuzzySearch ="%" + userNameSearch + "%";
		String sql= "SELECT park_id, name, location, establish_date, area, visitors, description FROM park WHERE name ilike ?;";
		SqlRowSet results = dao.queryForRowSet(sql, fuzzySearch);
		while (results.next()) {
			Park foundPark = mapToPark(results);
			foundParks.add(foundPark);
		}
		return foundParks;
	}

	@Override
	public List<Park> searchParksByLocation(String userLocationSearch) {
		List <Park> foundParks = new ArrayList<Park> ();
		String fuzzySearch ="%" + userLocationSearch + "%";
		String sql= "SELECT park_id, name, location, establish_date, area, visitors, description FROM park WHERE location ilike ?;";
		SqlRowSet results = dao.queryForRowSet(sql, fuzzySearch);
		while (results.next()) {
			Park foundPark = mapToPark(results);
			foundParks.add(foundPark);
		}
		return foundParks;
	}

}
