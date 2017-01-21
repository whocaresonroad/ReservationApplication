package io.github.whocaresonroad.reservation;

/*
 * Helper class to keep the reservation lengths in the manager.
 */
public class ReservationLengthItem {

	private String length;

	ReservationLengthItem(String length) {
		this.length = length;
	}

	public String getLength() {
		return length;
	}
}
