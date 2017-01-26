package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ReservationLenghtItemTest {

	ReservationLengthItem item;

	@Before
	public void setUp() throws Exception {
		item = new ReservationLengthItem("The meaning of life");
	}

	@Test
	public void creation() {
		assertNotNull(item);
	}

	@Test
	public void value() {
		assertEquals("The meaning of life", item.getLength());
	}
}
