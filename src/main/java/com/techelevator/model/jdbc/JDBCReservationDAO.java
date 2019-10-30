package com.techelevator.model.jdbc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.model.Campground;
import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;

public class JDBCReservationDAO implements ReservationDAO{
	
	private JdbcTemplate dao;

	public JDBCReservationDAO(DataSource dataSource) {
		this.dao = new JdbcTemplate(dataSource);
	}

	@Override
	public List<Reservation> getAllReservationsByCampground(Campground campground) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		String sql = "SELECT reservation_id, site_id, name, from_date, to_date, create_date FROM reservation WHERE campground = ?;";
		SqlRowSet results = dao.queryForRowSet(sql, campground);
		while (results.next()) {
			Reservation foundRervation = mapToReservation(results);
			reservations.add(foundRervation);
		}
		return reservations;
	}

	private Reservation mapToReservation(SqlRowSet results) {
		Reservation r = new Reservation();
		r.setReservationId(results.getInt("reservation_id"));
		r.setSite_id(results.getInt("site_id"));
		r.setName(results.getString("name"));
		r.setFromDate(results.getDate("from_date"));
		r.setToDate(results.getDate("to_date"));
		r.setCreateDate(results.getDate("create_date"));
		return r;
	}

	@Override
	public int createReservation(Reservation reservation) {
		int reservationId = 0;
		try {
		String sql = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) "
				+ "VALUES (?,?,?,?,?) "
				+ "RETURNING reservation_id;";
		SqlRowSet results = dao.queryForRowSet(sql, reservation.getSite_id(), reservation.getName(), reservation.getFromDate(), reservation.getToDate(), reservation.getCreateDate());
		if (results.next()) {
			reservation.setReservationId(results.getInt("reservation_id"));
			reservationId = reservation.getReservationId();
		}
		} catch (Exception e){
			System.out.println("Failed to insert reservation");
		}
		return reservationId;
	}

	@Override
	public List<Site> seeAvailablereservations(int campground, String fromDate, String toDate) {
		List<Site> availableSites = new ArrayList<Site>();
		java.sql.Date sqlStartDate = java.sql.Date.valueOf(fromDate);
		java.sql.Date sqlEndDate = java.sql.Date.valueOf(toDate);
		String sql = "SELECT * FROM site WHERE campground_id = ? AND site.site_id "
				+ "NOT IN (SELECT reservation.site_id from reservation WHERE ? <= reservation.to_date "
				+ "and ? >= reservation.from_date) ORDER BY site_id LIMIT 5;";
		SqlRowSet results = dao.queryForRowSet(sql, campground, sqlStartDate, sqlEndDate);
		while (results.next()) {
			Site availableSite = mapToSite(results);
			availableSites.add(availableSite);
		}
		return availableSites;
	}
	
	@Override
	public List<Reservation> getUpcomingreservations(Park park) {
		List<Reservation> upcomingReservations = new ArrayList<>();
		Calendar c=new GregorianCalendar();
		c.add(Calendar.DATE, 30);
		Date currentDate = new Date();
		Date futureDate = c.getTime();
		String sql = "SELECT * FROM reservation "
				+ "JOIN site on site.site_id = reservation.site_id "
				+ "JOIN campground on campground.campground_id = site.campground_id "
				+ "JOIN park on park.park_id = campground.park_id "
				+ "WHERE reservation.from_date BETWEEN ? AND ? AND park.park_id = ? ORDER BY from_date;";
		SqlRowSet results = dao.queryForRowSet(sql, currentDate, futureDate, park.getId());
		while (results.next()) {
			Reservation r = mapToReservation(results);
			upcomingReservations.add(r);
		}
		return upcomingReservations;
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
