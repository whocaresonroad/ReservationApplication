package io.github.whocaresonroad.reservation;

import java.util.Date;
import java.util.List;

/*
 * Custom repository for Spring, implemented in ReservationRepositoryImpl.
 */
public interface ReservationRepositoryCustom {

	/*
	 * Save the reservation and check that it is not overlapping.
	 * @return The reservation if it was saved, null otherwise.
	 */
	public Reservation saveAndCheck(Reservation reservation);

	/*
	 * Generate a list of available slots based on slot time settings starting from the given time.
	 * Slots have desired length that fit in the reservations on db.
	 */
	public List<ReservationSlot> getFreeReservationSlots(Date startdate, int slotMinutes);
}
