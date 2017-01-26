package io.github.whocaresonroad.reservation;

import java.util.Date;

import io.github.whocaresonroad.util.DateTimeUtil;

/*
 * Helper class to keep the reservation in the manager.
 */
public class ReservationRepresentation {
	private String id;
	private String firstName;
	private String lastName;
	private String address;
	private String postalCode;
	private String city;
	private String startTime;
	private String endTime;
	private String createdDate;
	private String modifiedDate;

	ReservationRepresentation(Reservation reservation, String formatString) {
		//  DB managed fields can be null
		String id = reservation.getId();
		if (id != null) {
			this.id = id; 
		} else {
			this.id = "";
		}
		firstName = reservation.getFirstName();
		lastName = reservation.getLastName();
		address = reservation.getAddress();
		postalCode = reservation.getPostalCode();
		city = reservation.getCity();
		startTime = DateTimeUtil.format(reservation.getStartTime(),formatString);
		endTime = DateTimeUtil.format(reservation.getEndTime(),formatString);

		//  DB managed fields can be null
		Date date = reservation.getCreatedDate();
		if (date != null) {
			createdDate = DateTimeUtil.format(date,formatString);
		}
		else {
			createdDate = "";
		}

		//  DB managed fields can be null
		date = reservation.getModifiedDate();
		if (date != null) {
			modifiedDate = DateTimeUtil.format(date,formatString);
		}
		else {
			modifiedDate = "";
		}
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getAddress() {
		return address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getCity() {
		return city;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public String getModifiedDate() {
		return modifiedDate;
	}
}
