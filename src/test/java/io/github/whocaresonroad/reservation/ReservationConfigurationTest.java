package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationConfigurationTest {

	@Autowired
	ReservationConfiguration rc;

	@Autowired
	MongoTemplate mongoTemplate;

	@Test
	public void contextLoads() {
	}

	@Test
	public void configurationFound() {
		assertNotNull(rc);
	}

	@Test
	public void mongoFound() {
		assertNotNull(mongoTemplate);
	}

	@Test
	public void databaseName() {
		assertEquals("ReservationDatabase", rc.getDatabaseName());
	}

	@Test
	public void reservationSlotLength() {
		assertEquals(15, rc.getMinimumReservationSlotLenghtInMinutes());
	}

	@Test
	public void reservationSlotLengths() {
		int[] lenghts = { 15, 30, 45, 60 };

		assertArrayEquals(lenghts, rc.getReservationSlotLenghtsInMinutes());
	}

	@Test
	public void reservationFreeSlots() {
		assertEquals(30, rc.getNumberOfFreeSlotsToShow());
	}

	@Test
	public void timeFormat() {
		assertEquals("HH:mm", rc.getTimeFormat());
	}

	@Test
	public void dateFormat() {
		assertEquals("dd.M.yyyy HH:mm", rc.getDateFormat());
	}

	@Test
	public void changeReservationFreeSlots() {
		assertTrue(rc.setNumberOfFreeSlotsToShow(42));
		assertEquals(42, rc.getNumberOfFreeSlotsToShow());
		assertTrue(rc.setNumberOfFreeSlotsToShow(30));
		assertEquals(30, rc.getNumberOfFreeSlotsToShow());
	}

	@Test
	public void changeNegativeReservationFreeSlots() {
		assertTrue(!rc.setNumberOfFreeSlotsToShow(-42));
		assertEquals(30, rc.getNumberOfFreeSlotsToShow());
	}

	@Test
	public void addAndRemoveReservationSlotLengths() {
		int[] oldLenghts = { 15, 30, 45, 60 };
		int[] newLenghts = { 15, 30, 42, 45, 60 };
		
		assertTrue(rc.addReservationSlotLenghtsInMinutes(42));
		assertArrayEquals(newLenghts, rc.getReservationSlotLenghtsInMinutes());

		assertTrue(rc.removeReservationSlotLenghtsInMinutes(42));
		assertArrayEquals(oldLenghts, rc.getReservationSlotLenghtsInMinutes());
	}

	@Test
	public void addNegativeReservationSlotLengths() {
		int[] lenghts = { 15, 30, 45, 60 };

		assertTrue(!rc.addReservationSlotLenghtsInMinutes(-42));
		assertArrayEquals(lenghts, rc.getReservationSlotLenghtsInMinutes());
	}

	@Test
	public void removeNegativeReservationSlotLengths() {
		int[] lenghts = { 15, 30, 45, 60 };

		assertTrue(!rc.addReservationSlotLenghtsInMinutes(-42));
		assertArrayEquals(lenghts, rc.getReservationSlotLenghtsInMinutes());
	}
}

//	org.junit.Assert.