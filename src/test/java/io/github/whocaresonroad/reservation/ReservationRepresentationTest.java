package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import io.github.whocaresonroad.util.DateTimeUtil;

public class ReservationRepresentationTest {

	ReservationRepresentation representation;
	ReservationRepresentation valuedRepresentation;
	Date now;
	Date end;
	String nowString;
	String endString;

	@Before
	public void setUp() throws Exception {
		Reservation reservation = new Reservation();
		representation = new ReservationRepresentation(reservation, "dd.M.yyyy HH:mm");

		now = new Date();
		end = DateTimeUtil.advance(now,Calendar.MINUTE,30);

		nowString = DateTimeUtil.format(now, "dd.M.yyyy HH:mm");
		endString = DateTimeUtil.format(end, "dd.M.yyyy HH:mm");

		Reservation valuedReservation = new Reservation();
		valuedReservation.setFirstName("Ford");
		valuedReservation.setLastName("Prefect");
		valuedReservation.setAddress("Café Lou");
		valuedReservation.setCity("Gretchen Town");
		valuedReservation.setPostalCode("New Betel");
		valuedReservation.setStartTime(now);
		valuedReservation.setEndTime(end);
		valuedRepresentation = new ReservationRepresentation(valuedReservation, "dd.M.yyyy HH:mm");
	}

	@Test
	public void id() {
		assertNotNull(representation.getId());
	}

	@Test
	public void created() {
		assertNotNull(representation.getCreatedDate());
	}

	@Test
	public void modified() {
		assertNotNull(representation.getModifiedDate());
	}

	@Test
	public void firstName() {
		assertEquals("",representation.getFirstName());
		assertEquals("Ford",valuedRepresentation.getFirstName());
	}

	@Test
	public void lastName() {
		assertEquals("",representation.getLastName());
		assertEquals("Prefect",valuedRepresentation.getLastName());
	}

	@Test
	public void address() {
		assertEquals("",representation.getAddress());
		assertEquals("Café Lou",valuedRepresentation.getAddress());
	}

	@Test
	public void city() {
		assertEquals("",representation.getCity());
		assertEquals("Gretchen Town",valuedRepresentation.getCity());
	}

	@Test
	public void postalCode() {
		assertEquals("",representation.getPostalCode());
		assertEquals("New Betel",valuedRepresentation.getPostalCode());
	}

	@Test
	public void startTime() {
		assertNotNull(representation.getStartTime());
		assertEquals(nowString,valuedRepresentation.getStartTime());
	}

	@Test
	public void endTime() {
		assertNotNull(representation.getEndTime());
		assertEquals(endString,valuedRepresentation.getEndTime());
	}
}
