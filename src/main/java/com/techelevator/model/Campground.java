package com.techelevator.model;

import java.math.BigDecimal;
import java.util.List;

public class Campground {
	private int campgroundId;
	private int parkId;
	private String name;
	private String openMonth;
	private String closeMonth;
	private BigDecimal dailyFee;
	private List<Site> sites;

	public Campground() {

	}

	public Campground(String name, String openMonth, String closeMonth, BigDecimal dailyFee) {
		this.name = name;
		this.openMonth = openMonth;
		this.closeMonth = closeMonth;
		this.dailyFee = dailyFee;
	}

	public int getId() {
		return campgroundId;
	}

	public void setId(int id) {
		this.campgroundId = id;
	}

	public int getParkId() {
		return parkId;
	}

	public void setParkId(int parkId) {
		this.parkId = parkId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOpenMonth() {
		return openMonth;
	}

	public void setOpenMonth(String openMonth) {
		this.openMonth = openMonth;
	}

	public String getCloseMonth() {
		return closeMonth;
	}

	public void setCloseMonth(String closeMonth) {
		this.closeMonth = closeMonth;
	}

	public BigDecimal getDailyFee() {
		return dailyFee;
	}

	public void setDailyFee(BigDecimal dailyFee) {
		this.dailyFee = dailyFee;
	}

	public String toString() {
		return String.format("%-20s %-5s %-5s $%-5.2f", name, openMonth, closeMonth, dailyFee);
	}

	public List<Site> getSites() {
		return sites;
	}

	public void setSites(List<Site> sites) {
		this.sites = sites;
	}

}
