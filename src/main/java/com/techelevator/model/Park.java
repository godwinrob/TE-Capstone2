package com.techelevator.model;

import java.util.Date;
import java.util.List;

public class Park {
	private int id;
	private String name;
	private String location;
	private Date estDate;
	private int area;
	private int annualVisitorCount;
	private String description;
	private List<Campground> campgrounds;

	public Park() {

	}

	public Park(String name, String location, Date estDate, int area, int annualVisitorCount, String description) {
		this.name = name;
		this.location = location;
		this.estDate = estDate;
		this.area = area;
		this.annualVisitorCount = annualVisitorCount;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Date getEstDate() {
		return estDate;
	}

	public void setEstDate(Date estDate) {
		this.estDate = estDate;
	}

	public int getArea() {
		return area;
	}

	public void setArea(int area) {
		this.area = area;
	}

	public int getAnnualVisitorCount() {
		return annualVisitorCount;
	}

	public void setAnnualVisitorCount(int annualVisitorCount) {
		this.annualVisitorCount = annualVisitorCount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String toString() {
		return name;
	}

	public List<Campground> getCampgrounds() {
		return campgrounds;
	}

	public void setCampgrounds(List<Campground> campgrounds) {
		this.campgrounds = campgrounds;
	}

}
