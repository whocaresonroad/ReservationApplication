package io.github.whocaresonroad.reservation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

/*
 * Spring configuration class.
 */
@Configuration
@EnableMongoAuditing
public class ReservationConfiguration extends AbstractMongoConfiguration {

	/*
	 * Format for date and time.
	 */
	private final String dateFormat = "dd.M.yyyy HH:mm";

	/*
	 * Format for time
	 */
	private final String timeFormat = "HH:mm";

	/*
	 * All possible reservation lengths in minutes. The first one is also the
	 * minimum length.
	 */
	private int[] reservationSlotLenghtsInMinutes = { 15, 30, 45, 60 };

	/*
	 * Number of free slots shown in the reservation view
	 */
	private int numberOfFreeSlotsToShow = 30;

	/*
	 * Default database name.
	 * 
	 * @see org.springframework.data.mongodb.config.AbstractMongoConfiguration#
	 * getDatabaseName()
	 */
	@Override
	public String getDatabaseName() {
		return "ReservationDatabase";
	}

	/*
	 * Create MongoDB client.
	 * 
	 * @see
	 * org.springframework.data.mongodb.config.AbstractMongoConfiguration#mongo(
	 * )
	 */
	@Override
	@Bean
	public Mongo mongo() throws Exception {
		return new MongoClient("localhost");
	}

	/*
	 * Return the possible reservation lengths in minutes.
	 */
	public int[] getReservationSlotLenghtsInMinutes() {
		return reservationSlotLenghtsInMinutes;
	}

	/*
	 * Return the minimum reservation length.
	 */
	public int getMinimumReservationSlotLenghtInMinutes() {
		return reservationSlotLenghtsInMinutes[0];
	}

	/*
	 * Format string for dates.
	 */
	public String getDateFormat() {
		return dateFormat;
	}

	/*
	 * Format string for times.
	 */
	public String getTimeFormat() {
		return timeFormat;
	}

	/*
	 * Return the number of free slot to show
	 */
	public int getNumberOfFreeSlotsToShow() {
		return numberOfFreeSlotsToShow;
	}

	/*
	 * Add new length to the array in correct place
	 */
	public boolean addReservationSlotLenghtsInMinutes(int newLength) {
		// Check that the value doesn't exist already
		for (int i = 0; i < reservationSlotLenghtsInMinutes.length; i++) {
			if (reservationSlotLenghtsInMinutes[i] == newLength) {
				// Already exist, don't add
				return false;
			}
		}

		// Create new array
		int[] newLenghts = new int[reservationSlotLenghtsInMinutes.length + 1];

		// Iterate the old array
		for (int i = 0; i < reservationSlotLenghtsInMinutes.length; i++) {
			// If old value is smaller, add it, otherwise add new value and take
			// the old value as new value
			if (reservationSlotLenghtsInMinutes[i] < newLength) {
				newLenghts[i] = reservationSlotLenghtsInMinutes[i];
			} else {
				newLenghts[i] = newLength;
				newLength = reservationSlotLenghtsInMinutes[i];
			}
		}

		// Add the last value and change the array
		newLenghts[reservationSlotLenghtsInMinutes.length] = newLength;
		reservationSlotLenghtsInMinutes = newLenghts;

		// TODO: add to the database

		return true;
	}

	public boolean removeReservationSlotLenghtsInMinutes(int lengthToRemove) {
		// Only remove if there is at least one left after removal
		if (reservationSlotLenghtsInMinutes.length >= 2) {
			// First create new array
			int[] newLenghts = new int[reservationSlotLenghtsInMinutes.length-1];
	
			// Iterate array
			int j = 0;
			for (int i = 0; i < reservationSlotLenghtsInMinutes.length; i++) {
				// Add old values until except the removed value
				if (reservationSlotLenghtsInMinutes[i] != lengthToRemove) {
					newLenghts[j] = reservationSlotLenghtsInMinutes[i];
					j++;
				}
			}
	
			// Change the array
			reservationSlotLenghtsInMinutes = newLenghts;
	
			// TODO: add to the database

			return true;
		}

	return false;
	}

	public void setNumberOfFreeSlotsToShow(int slots) {
		numberOfFreeSlotsToShow = slots;

		// TODO: add to the database
		}
}