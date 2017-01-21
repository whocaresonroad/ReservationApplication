package io.github.whocaresonroad.reservation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import io.github.whocaresonroad.util.DateTimeUtil;

/*
 * Spring repository implementation, contains all the db access.
 */
public class ReservationRepositoryImpl implements ReservationRepositoryCustom {

	/*
	 * Spring default repository.
	 */
	@Autowired
	ReservationRepository repository;

	/*
	 * MongoDB database access.
	 */
	@Autowired
	MongoTemplate mongoTemplate;

	/*
	 * Configuration class for getting settings.
	 */
	@Autowired
	ReservationConfiguration reservationConfiguration;

	/*
	 * Implementation
	 * @see io.github.whocaresonroad.reservation.ReservationRepositoryCustom#saveAndCheck(io.github.whocaresonroad.reservation.Reservation)
	 */
	public Reservation saveAndCheck(Reservation reservation) {
		// Save the reservation
		Reservation savedReservation = repository.save(reservation);

		// Make sure that the reservation time is still free
		if (savedReservation != null) {
			if (hasEarlierOverlappingReservation(savedReservation)) {
				// Reservation time was already taken, remove this and inform caller
				repository.delete(savedReservation);
				return null;
			}
		}

		return savedReservation;
	}

	/*
	 * implementation
	 * @see io.github.whocaresonroad.reservation.ReservationRepositoryCustom#getFreeReservationSlots(java.util.Date, int)
	 */
	public List<ReservationSlot> getFreeReservationSlots(Date startdate, int slotMinutes) {
		List<ReservationSlot> freeSlots = new ArrayList<ReservationSlot>();
		int minimumSlotMinutes = reservationConfiguration.getMinimumReservationSlotLenghtInMinutes();
		int slotsToCreate = reservationConfiguration.getNumberOfFreeSlotsToShow();

		// Move start time to next minimum slot time
		Date earliestDate = getNextSlotDate(startdate, minimumSlotMinutes);
		Query query = new Query();

		// Get all reservations after the start time
		query.addCriteria(where("startTime").gte(earliestDate));
		query.with(new Sort(Sort.Direction.ASC, "startTime"));
		List<Reservation> reservationsAfter = mongoTemplate.find(query, Reservation.class);

		Date slotStart = earliestDate;
		Date slotEnd;

		// Create free slots
		for (int i = 1; i <= slotsToCreate; i++) {
			//	Advance the end time by slot minutes
			slotEnd = DateTimeUtil.advance(slotStart,Calendar.MINUTE,slotMinutes);

			boolean found = false;

			while (!found) {
				// Assume we found it
				found = true;
				for (Reservation reservation : reservationsAfter) {
					// Check if the slot already has reservation
					if (((slotStart.getTime() >= reservation.getStartTime().getTime())
						&& (slotStart.getTime() < reservation.getEndTime().getTime()))
						||
						((slotEnd.getTime() > reservation.getStartTime().getTime())
						&& (slotEnd.getTime() <= reservation.getEndTime().getTime()))
						||
						((slotStart.getTime() <= reservation.getStartTime().getTime())
						&& (slotEnd.getTime() >= reservation.getEndTime().getTime()))) {
						// Slot taken
						found = false;
						// Advance start time by minimum slot minutes
						slotStart = DateTimeUtil.advance(slotStart,Calendar.MINUTE,minimumSlotMinutes);

						//	Advance the end time by slot minutes
						slotEnd = DateTimeUtil.advance(slotStart,Calendar.MINUTE,slotMinutes);

						// break the for loop
						break;
					}
				}
			}

			// Free slot found
			ReservationSlot newSlot = new ReservationSlot(slotStart,slotMinutes,DateTimeUtil.format(slotStart,reservationConfiguration.getDateFormat()));
			freeSlots.add(newSlot);

			// Advance start by minimum slot minutes
			slotStart = DateTimeUtil.advance(slotStart,Calendar.MINUTE,minimumSlotMinutes);
		}

		return freeSlots;
	}

	/*
	 * Check if the db has reservation which conflicts with the given one and was created earlier.
	 * @return True if there was conflicting reservation, false otherwise.
	 */
	public boolean hasEarlierOverlappingReservation(Reservation reservation) {

		// Can't end during our reservation if it was reserved before ours
		Long overlapped = mongoTemplate.count(query(where("endTime").gt(reservation.getStartTime())
				.andOperator(Criteria.where("endTime").lte(reservation.getEndTime())).and("createdDate")
				.lt(reservation.getCreatedDate())), Reservation.class);

		if (overlapped > 0) {
			return true;
		}

		// Can't start during our reservation if it was reserved before ours
		overlapped = mongoTemplate.count(query(where("startTime").gte(reservation.getStartTime())
				.andOperator(Criteria.where("startTime").lt(reservation.getEndTime())).and("createdDate")
				.lt(reservation.getCreatedDate())), Reservation.class);

		if (overlapped > 0) {
			return true;
		}

		// Can't start and end during our reservation if it was reserved before ours
		overlapped = mongoTemplate.count(query(where("startTime").lte(reservation.getStartTime())
				.andOperator(Criteria.where("endTime").gte(reservation.getEndTime())).and("createdDate")
				.lt(reservation.getCreatedDate())), Reservation.class);

		if (overlapped > 0) {
			return true;
		}
		return false;
	}

	/*
	 * Round the time to given next slot minutes.
	 */
	private Date getNextSlotDate(Date startdate, int slotMinutes) {
		Calendar start = Calendar.getInstance();
		start.setTime(startdate);

		// Reset the minutes to slot interval and advance to next slot start
		start.set(Calendar.MINUTE, (start.get(Calendar.MINUTE) / slotMinutes) * slotMinutes);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		start.add(Calendar.MINUTE, slotMinutes);

		return start.getTime();
	}
}
