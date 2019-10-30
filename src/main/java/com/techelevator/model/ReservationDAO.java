package com.techelevator.model;

import java.util.List;

public interface ReservationDAO {
	
	public List<Reservation> getAllReservationsByCampground(Campground campground);
	
	public int createReservation(Reservation reservation);
	
	public List<Site> seeAvailablereservations(int campground, String fromDate, String toDate);

	public List<Reservation> getUpcomingreservations(Park park);

}
