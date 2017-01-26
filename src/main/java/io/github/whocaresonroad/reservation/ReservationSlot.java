package io.github.whocaresonroad.reservation;

import java.util.Calendar;
import java.util.Date;

import io.github.whocaresonroad.util.DateTimeUtil;

/*
 * Holder class for holding the slot start time and length and presentation string for the start time
 */
public class ReservationSlot {

	/*
	 * Start time of the reservation slot.
	 */
	private Date startTime;

	/*
	 * Length of the slot in minutes.
	 */
	private int slotLenghtInMinutes;

	/*
	 * String to show in the UI.
	 */
	private String displayString;

	public ReservationSlot(Date startTime, int slotLenght, String displayString) {
		this.startTime = startTime;
		this.slotLenghtInMinutes = slotLenght;
		this.displayString = displayString;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return DateTimeUtil.advance(startTime, Calendar.MINUTE, slotLenghtInMinutes);
	}

	public String getDisplayString() {
		return displayString;
	}

	public void setDisplayString(String displayString) {
		this.displayString = displayString;
	}

	public int getSlotLenght() {
		return slotLenghtInMinutes;
	}

	public void setSlotLenght(int slotLenght) {
		this.slotLenghtInMinutes = slotLenght;
	}
}
