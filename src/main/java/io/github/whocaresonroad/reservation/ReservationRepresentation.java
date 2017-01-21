package io.github.whocaresonroad.reservation;

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
		id = (reservation.getId());
		firstName = reservation.getFirstName();
		lastName = reservation.getLastName();
		address = reservation.getAddress();
		postalCode = reservation.getPostalCode();
		city = reservation.getCity();
		startTime = DateTimeUtil.format(reservation.getStartTime(),formatString);
		endTime = DateTimeUtil.format(reservation.getEndTime(),formatString);
		createdDate = DateTimeUtil.format(reservation.getCreatedDate(),formatString);
		modifiedDate = DateTimeUtil.format(reservation.getModifiedDate(),formatString);
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
