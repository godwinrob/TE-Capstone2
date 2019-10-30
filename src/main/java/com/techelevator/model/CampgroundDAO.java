package com.techelevator.model;

import java.util.List;

public interface CampgroundDAO {
	
	public List<Campground> getAllCampgrounds(Park park);

	public Campground getCampgroundById(int campgroundSelection);

}
