package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ReservationSlotTest {

	Date now;
	Date end;
	ReservationSlot slot;

	@Before
	public void setUp() throws Exception {
		now = new Date();
		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(now);
		endCalendar.add(Calendar.MINUTE, 30);
		end = endCalendar.getTime();
		slot = new ReservationSlot(now, 30, "30");
	}

	@Test
	public void creation() {
		assertNotNull(slot);
	}

	@Test
	public void start() {
		assertEquals(now, slot.getStartTime());
	}

	@Test
	public void slotlenght() {
		assertEquals(30, slot.getSlotLenght());
	}

	@Test
	public void display() {
		assertEquals("30", slot.getDisplayString());
	}

	@Test
	public void end() {
		assertEquals(end, slot.getEndTime());
	}

	@Test
	public void setDisplay() {
		slot.setDisplayString("42");
		assertEquals("42", slot.getDisplayString());
		slot.setDisplayString("30");
	}

	@Test
	public void setLenght() {
		slot.setSlotLenght(42);
		assertEquals(42, slot.getSlotLenght());
		slot.setSlotLenght(30);
	}
}
