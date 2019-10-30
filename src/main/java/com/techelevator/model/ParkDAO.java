package com.techelevator.model;

import java.util.List;

public interface ParkDAO {
	
	public List<Park> getAllParks(); // pass in nothing
	
	public List<Park> searchParksByName(String userNameSearch); // pass in name of park from user
	
	public List<Park> searchParksByLocation(String userLocationSearch); // pass in state of park from user

}
