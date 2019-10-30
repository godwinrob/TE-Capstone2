package com.techelevator;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.model.Campground;
import com.techelevator.model.CampgroundDAO;
import com.techelevator.model.Park;
import com.techelevator.model.ParkDAO;
import com.techelevator.model.Reservation;
import com.techelevator.model.ReservationDAO;
import com.techelevator.model.Site;
import com.techelevator.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.model.jdbc.JDBCParkDAO;
import com.techelevator.model.jdbc.JDBCReservationDAO;
//import com.techelevator.model.SiteDAO;
//import com.techelevator.model.jdbc.JDBCSiteDAO;
import com.techelevator.view.Menu;


/**
 * A Campground reservation system written to help develop skills interacting with a Database with Java
 * Authors: Rob Godwin, Alex Simon
 * Version: 1.00
 * Date: 10/30/2019
 */
public class CampgroundCLI {

	private static final String MAIN_MENU_OPTION_PARKS = "View All Parks"; // THESE MAY BECOME SUBMENUS
	private static final String MAIN_MENU_OPTION_PARKS_BY_NAME = "Search parks by name";
	private static final String MAIN_MENU_OPTION_PARKS_BY_LOCATION = "Search parks by location";
	private static final String MAIN_MENU_OPTION_EXIT = "EXIT";
	private static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS,
			MAIN_MENU_OPTION_PARKS_BY_NAME, MAIN_MENU_OPTION_PARKS_BY_LOCATION, MAIN_MENU_OPTION_EXIT };

	private static final String CAMPGROUND_MENU_OPTION_CAMPGROUNDS = "View Campgrounds";
	private static final String CAMPGROUND_MENU_OPTION_RESERVATION = "Search for Reservation";
	private static final String CAMPGROUND_MENU_OPTION_RETURN = "Return to Main Menu";
	private static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_CAMPGROUNDS,
			CAMPGROUND_MENU_OPTION_RESERVATION,	CAMPGROUND_MENU_OPTION_RETURN };

	private static final String SITE_MENU_OPTION_RESERVATION = "Search for Available Reservation";
	private static final String SITE_MENU_OPTION_RETURN = "Return to Campground Menu";
	private static final String[] SITE_MENU_OPTIONS = new String[] { SITE_MENU_OPTION_RESERVATION,
			SITE_MENU_OPTION_RETURN };

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private ReservationDAO reservationDAO;
	// private SiteDAO siteDAO;

	public static void main(String[] args) {

		CampgroundCLI application = new CampgroundCLI();
		application.run();

	}

	public CampgroundCLI() {
		this.menu = new Menu(System.in, System.out);
		// create your DAOs here
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		parkDAO = new JDBCParkDAO(dataSource);
		campgroundDAO = new JDBCCampgroundDAO(dataSource);
		// siteDAO = new JDBCSiteDAO(dataSource);
		reservationDAO = new JDBCReservationDAO(dataSource);
	}

	public void run() {
		while (true) {
			printHeading("Main Menu");
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
			if (choice.equals(MAIN_MENU_OPTION_PARKS)) {
				handleParks();
			} else if (choice.equals(MAIN_MENU_OPTION_PARKS_BY_NAME)) {
				handleParkSearchName();
			} else if (choice.equals(MAIN_MENU_OPTION_PARKS_BY_LOCATION)) {
				handleParkSearchLocation();
			} else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
				handleExit();
			}
		}
	}

	private void campgroundMenu(Park park) {
		printHeading("Search for Campground Reservation");
		// List<Campground> campgroundsForPark = park.getCampgrounds();
		String choice = (String) menu.getChoiceFromOptions(CAMPGROUND_MENU_OPTIONS);
		if (choice.equals(CAMPGROUND_MENU_OPTION_CAMPGROUNDS)) {
			handleAllCampgroundsInPark(park);
		} else if (choice.equals(CAMPGROUND_MENU_OPTION_RESERVATION)) {
			viewUpcomingReservations(park);
		} else if (choice.equals(CAMPGROUND_MENU_OPTION_RETURN)) {

		}
	}

	private void siteMenu(Park park) {
		printHeading("Select a Command");
		String choice = (String) menu.getChoiceFromOptions(SITE_MENU_OPTIONS);
		if (choice.equals(SITE_MENU_OPTION_RESERVATION)) {
			handleCampgroundReservations(park);
		} else if (choice.equals(SITE_MENU_OPTION_RETURN)) {
			campgroundMenu(park);
		}
	}

	// Park search related methods

	private void handleParks() {
		Park selectedPark = handleListAllParks();
		showParkInformation(selectedPark);
		campgroundMenu(selectedPark);
	}

	private Park handleListAllParks() {
		printHeading("Select a park for further details");
		List<Park> allParks = parkDAO.getAllParks();
		// printListPark(allParks);
		return (Park) menu.getChoiceFromOptions(allParks.toArray());
	}

	private void handleParkSearchName() {
		Park selectedPark = handleSearchParksByName();
		if (selectedPark != null) {
			showParkInformation(selectedPark);
			campgroundMenu(selectedPark);
		}
	}

	private Park handleSearchParksByName() {
		printHeading("Park search by name");
		String parkSearchName = getUserInput("Enter park name to search for ");
		List<Park> parks = parkDAO.searchParksByName(parkSearchName);
		if (parks.size() > 0) {
			return (Park) menu.getChoiceFromOptions(parks.toArray());
		} else {
			System.out.println("No results found for " + parkSearchName);
		}
		return null;
	}

	private void handleParkSearchLocation() {
		Park selectedPark = handleSearchParkByLocation();
		if (selectedPark != null) {
			showParkInformation(selectedPark);
			campgroundMenu(selectedPark);
		}
	}

	private Park handleSearchParkByLocation() {
		printHeading("Park search by State");
		String parkSearchLocation = getUserInput("Enter state to search for parks ");
		List<Park> parks = parkDAO.searchParksByLocation(parkSearchLocation);
		if (parks.size() > 0) {
			return (Park) menu.getChoiceFromOptions(parks.toArray());
		} else {
			System.out.println("No results found for " + parkSearchLocation);
		}
		return null;
	}

	private void handleAllCampgroundsInPark(Park park) {
		printHeading(park.getName() + " Campgrounds");
		System.out.printf("   %-32s %-10s %-10s %-5s", "Name", "Open", "Close", "Daily Fee\n");
		List<Campground> allCampGrounds = campgroundDAO.getAllCampgrounds(park);
		int count = 1;
		for (Campground campground : allCampGrounds) {
			System.out.printf("#%d %-32s %-10s %-10s $%-5.2f\n", count, campground.getName(),
					Month.of(Integer.parseInt(campground.getOpenMonth())).name().toString(),
					Month.of(Integer.parseInt(campground.getCloseMonth())).name().toString(), campground.getDailyFee());
			count++;
		}
		siteMenu(park);
	}

	private List<Site> handleCampgroundReservations(Park park) {
		Map<Integer, Integer> sites = new HashMap<Integer, Integer>();
		printHeading("Search for Campground Reservation");
		System.out.printf("   %-32s %-5s %-5s %-5s", "Name", "Open", "Close", "Daily Fee\n");
		List<Campground> allCampGrounds = campgroundDAO.getAllCampgrounds(park);
		int count = 1;
		for (Campground campground : allCampGrounds) {
			System.out.printf("#%d %-32s %-5s %-5s $%-5.2f\n", count, campground.getName(), campground.getOpenMonth(),
					campground.getCloseMonth(), campground.getDailyFee());
			int campgroundId = campground.getId();
			sites.put(count, campgroundId);
			count++;
		}
		List<Site> availableSites = getSitesWithoutReservationInRange(sites, park);
		return availableSites;
	}

	@SuppressWarnings("resource")
	private List<Site> getSitesWithoutReservationInRange(Map<Integer, Integer> sites, Park park) {
		List<Site> availableSites = null;
		int reservationId = 0;
		Date currentDate = new Date();
		try {
			Scanner input = new Scanner(System.in);
			System.out.println("Which Campground (enter 0 to cancel)? ");
			int campgroundSelection = Integer.parseInt(input.nextLine());
			if (campgroundSelection == 0) {
				campgroundMenu(park);
			} else if (campgroundSelection < 0) {
				System.out.println("Selection must be greater than 0. Returning to campground menu.");
				campgroundMenu(park);
			} else {
				int campgroundId = sites.get(campgroundSelection);
				System.out.println("What is the arrival date? (yyyy-MM-dd)");
				String reservationFromDate = input.nextLine();
				while (parseStringToDate(reservationFromDate).before(currentDate)) {
					System.out.println("Arrival date must be in the future!");
					reservationFromDate = input.nextLine();
				}
				System.out.println("What is the departure date? (yyyy-MM-dd)");
				String reservationToDate = input.nextLine();
				while (parseStringToDate(reservationToDate).before(parseStringToDate(reservationFromDate))) {
					System.out.println("Arrival date cannot be before departure date!");
					System.out.println("What is the arrival date? (yyyy-MM-dd)");
					reservationFromDate = input.nextLine();
					while (parseStringToDate(reservationFromDate).before(currentDate)) {
						System.out.println("Arrival date must be in the future!");
						reservationFromDate = input.nextLine();
					}
					System.out.println("What is the departure date? (yyyy-MM-dd)");
					reservationToDate = input.nextLine();
				}
				availableSites = reservationDAO.seeAvailablereservations(campgroundId, reservationFromDate,
						reservationToDate);
				while (availableSites.size() == 0) {
					System.out.println(
							"No available sites found for given date range. please enter a new date range or 0 to cancel");
					System.out.println("What is the arrival date? (yyyy-MM-dd)");
					reservationFromDate = input.nextLine();
					if (reservationFromDate.substring(0, 1).contains("0")) {
						campgroundMenu(park);
					}
					System.out.println("What is the departure date? (yyyy-MM-dd)");
					reservationToDate = input.nextLine();
					availableSites = reservationDAO.seeAvailablereservations(campgroundId, reservationFromDate,
							reservationToDate);
				}
				List<Site> topFiveSites = handleSites(campgroundId, availableSites, reservationFromDate,
						reservationToDate);
				reservationId = makeReservation(reservationFromDate, reservationToDate, park, topFiveSites);
				if (reservationId != 0) {
					System.out.println("The reservation has been made and the confirmation id is " + reservationId);
				} else {
					System.out.println("Something went wrong! Reservation not created. Error: " + reservationId);
				}
			}

		} catch (Exception e) {
			System.out.println("Something was entered incorrectly. Reservation not created. Returning to Campground Menu.");
			campgroundMenu(park);
		}
		return availableSites;
	}

	private List<Site> handleSites(int campgroundId, List<Site> sites, String fromDate, String toDate)
			throws ParseException {
		int days = daysBetween(parseStringToDate(fromDate), parseStringToDate(toDate));
		Campground selectedCampround = campgroundDAO.getCampgroundById(campgroundId);
		BigDecimal totalCostForStay = selectedCampround.getDailyFee().multiply(new BigDecimal(days));
		printHeading("Results Matching Your Search Criteria");
		System.out.printf("%-15s %-15s %-15s %-15s %-15s %-15s\n", "Site No.", "Max Occup.", "Accessible?",
				"Max RC Length", "Utility", "Cost");
		for (Site site : sites) {
			String accessible = "";
			String rvLength = "";
			String utility = "";
			if (site.isAccessible()) {
				accessible = "Yes";
			} else {
				accessible = "No";
			}
			if (site.getMaxRvLength() < 1) {
				rvLength = "N/A";
			} else {
				rvLength = String.valueOf(site.getMaxRvLength());
			}
			if (site.isUtilities()) {
				utility = "Yes";
			} else {
				utility = "N/A";
			}
			System.out.printf("%-15d %-15d %-15s %-15s %-15s $%-15.2f\n", site.getSiteId(), site.getMaxOccupancy(),
					accessible, rvLength, utility, totalCostForStay);
		}
		return sites;
	}

	@SuppressWarnings("resource")
	private int makeReservation(String reservationFromDate, String reservationToDate, Park park,
			List<Site> topFiveSites) throws ParseException {
		int reservationId = 0;
		int siteIdSelection = 0;
		List<Integer> siteIds = new ArrayList<>();
		for (int i = 0; i < topFiveSites.size(); i++) {
			siteIds.add(topFiveSites.get(i).getSiteId());
		}
		System.out.println("Which site should be reserved? (enter 0 to cancel)");
		Date fromDate = parseStringToDate(reservationFromDate);
		Date toDate = parseStringToDate(reservationToDate);
		Date currentDate = new Date();
		try {
			Scanner input = new Scanner(System.in);
			siteIdSelection = Integer.parseInt(input.nextLine());
			if (siteIdSelection == 0) {
				System.out.println("Returning to campground menu.");
				campgroundMenu(park);
			} else if (siteIdSelection < 0) {
				System.out.println("Selection must be greater than 0. Returning to campground menu.");
				campgroundMenu(park);
			}
			while (!siteIds.contains(siteIdSelection)) {
				System.out.println("Not a valid Site selection. Enter a valid selection. (enter 0 to cancel)");
				siteIdSelection = Integer.parseInt(input.nextLine());
				if (siteIdSelection == 0) {
					campgroundMenu(park);
				}
			}
			System.out.println("What name should the reservation be made under?");
			String reservationName = input.nextLine();
			Reservation r = new Reservation();
			r.setSite_id(siteIdSelection);
			r.setName(reservationName);
			r.setFromDate(fromDate);
			r.setToDate(toDate);
			r.setCreateDate(currentDate);
			reservationId = reservationDAO.createReservation(r);
		} catch (Exception e) {
			System.out.println("Something went wrong creating your reservation!");
		}
		return reservationId;
	}

	private void viewUpcomingReservations(Park park) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		printHeading("Upcoming Reservations for park: " + park.getName());
		List<Reservation> upcomingReservations = reservationDAO.getUpcomingreservations(park);
		System.out.printf("\n%-8s %-35s %-12s %-8s", "Site_ID", "Reservation_Name", "Start_Date", "End_Date\n");
		for (Reservation reservation : upcomingReservations) {
			System.out.printf("%-8d %-35s %-12s %-8s\n", reservation.getSite_id(), reservation.getName(),
					sdf.format(reservation.getFromDate()), sdf.format(reservation.getToDate()));
		}
		campgroundMenu(park);
	}

	private Date parseStringToDate(String date) throws ParseException {
		String[] splitDate = date.split("-");
		String dateString = splitDate[2] + "/" + splitDate[1] + "/" + splitDate[0];
		return new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
	}

	public int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	private void showParkInformation(Park park) {
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String date = sdf.format(park.getEstDate());
		System.out.printf("\n%s\n", park.getName());
		System.out.printf("Location: %s\n", park.getLocation());
		System.out.printf("Established: %s\n", date);
		System.out.printf("Area: %s sq km\n", numberFormat.format(park.getArea()));
		System.out.printf("Annual Visitors: %s\n\n", numberFormat.format(park.getAnnualVisitorCount()));
		String str = park.getDescription();
		System.out.printf(wrapString(str, "\n", 80));
		System.out.println();
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public static String wrapString(String s, String deliminator, int length) {
		String result = "";
		int lastdelimPos = 0;
		for (String token : s.split(" ", -1)) {
			if (result.length() - lastdelimPos + token.length() > length) {
				result = result + deliminator + token;
				lastdelimPos = result.length() + 1;
			} else {
				result += (result.isEmpty() ? "" : " ") + token;
			}
		}
		return result;
	}

	private void handleExit() {

		System.out.println("\nHave a great day!");
		System.exit(0);
	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}
}
