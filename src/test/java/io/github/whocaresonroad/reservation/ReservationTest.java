package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import io.github.whocaresonroad.util.DateTimeUtil;

public class ReservationTest {

	Reservation emptyReservation;
	Reservation valuedReservation;
	Date now;
	Date end;

	@Before
	public void setUp() throws Exception {
		emptyReservation = new Reservation();

		now = new Date();
		end = DateTimeUtil.advance(now, Calendar.MINUTE, 30);
		valuedReservation = new Reservation();
		valuedReservation.setFirstName("Ford");
		valuedReservation.setLastName("Prefect");
		valuedReservation.setAddress("Café Lou");
		valuedReservation.setCity("Gretchen Town");
		valuedReservation.setPostalCode("New Betel");
		valuedReservation.setStartTime(now);
		valuedReservation.setEndTime(end);
	}

	@Test
	public void emptyContruction() {
		assertEquals("", emptyReservation.getFirstName());
		assertEquals("", emptyReservation.getLastName());
		assertEquals("", emptyReservation.getAddress());
		assertEquals("", emptyReservation.getCity());
		assertEquals("", emptyReservation.getPostalCode());
		assertNull(emptyReservation.getId());
		assertNull(emptyReservation.getCreatedDate());
		assertNull(emptyReservation.getModifiedDate());
		assertNotNull(emptyReservation.getStartTime());
		assertNotNull(emptyReservation.getEndTime());
	}

	@Test
	public void copyContruction() {
		Reservation copy = new Reservation(valuedReservation);

		assertEquals("Ford", copy.getFirstName());
		assertEquals("Prefect", copy.getLastName());
		assertEquals("Café Lou", copy.getAddress());
		assertEquals("Gretchen Town", copy.getCity());
		assertEquals("New Betel", copy.getPostalCode());
		assertNull(copy.getId());
		assertNull(copy.getCreatedDate());
		assertNull(copy.getModifiedDate());
		assertEquals(now, copy.getStartTime());
		assertEquals(end, copy.getEndTime());
	}

	@Test
	public void getSet() {
		Reservation reservation = new Reservation(valuedReservation);

		reservation.setFirstName("Mauno");
		assertEquals("Mauno", reservation.getFirstName());
		reservation.setLastName("Ahonen");
		assertEquals("Ahonen", reservation.getLastName());
		reservation.setAddress("Ohranjyvä");
		assertEquals("Ohranjyvä", reservation.getAddress());
		reservation.setCity("Tampere");
		assertEquals("Tampere", reservation.getCity());
		reservation.setPostalCode("33210");
		assertEquals("33210", reservation.getPostalCode());

		Date newNow = new Date();
		Date newEnd = DateTimeUtil.advance(newNow, Calendar.MINUTE, 30);
		reservation.setStartTime(newNow);
		assertEquals(newNow, reservation.getStartTime());
		reservation.setEndTime(newEnd);
		assertEquals(newEnd, reservation.getEndTime());

		assertNull(reservation.getId());
		assertNull(reservation.getCreatedDate());
		assertNull(reservation.getModifiedDate());
	}

}
