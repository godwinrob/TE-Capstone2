package com.techelevator.model;

import java.util.List;

public class Site {

	private int siteId;
	private int campgroundId;
	private int siteNumber;
	private int maxOccupancy;
	private boolean isAccessible;
	private int maxRvLength;
	private boolean utilities;
	private List<Reservation> reservations;

	public Site() {

	}

	public Site(int campgroundId, int siteNumber, int maxOccupancy, boolean isAccessible, int maxRvLength,
			boolean utilities) {
		this.campgroundId = campgroundId;
		this.siteNumber = siteNumber;
		this.maxOccupancy = maxOccupancy;
		this.isAccessible = isAccessible;
		this.maxRvLength = maxRvLength;
		this.utilities = utilities;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getCampgroundId() {
		return campgroundId;
	}

	public void setCampgroundId(int campgroundId) {
		this.campgroundId = campgroundId;
	}

	public int getSiteNumber() {
		return siteNumber;
	}

	public void setSiteNumber(int siteNumber) {
		this.siteNumber = siteNumber;
	}

	public int getMaxOccupancy() {
		return maxOccupancy;
	}

	public void setMaxOccupancy(int maxOccupancy) {
		this.maxOccupancy = maxOccupancy;
	}

	public boolean isAccessible() {
		return isAccessible;
	}

	public void setAccessible(boolean isAccessible) {
		this.isAccessible = isAccessible;
	}

	public int getMaxRvLength() {
		return maxRvLength;
	}

	public void setMaxRvLength(int maxRvLength) {
		this.maxRvLength = maxRvLength;
	}

	public boolean isUtilities() {
		return utilities;
	}

	public void setUtilities(boolean utilities) {
		this.utilities = utilities;
	}

	public String toString() {
		return String.format("TODO Enter toString for Site.java");

	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

}
