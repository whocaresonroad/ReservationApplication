package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.github.whocaresonroad.util.DateTimeUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReservationRepositoryTest {

	@Autowired
	ReservationRepository reservationRepository;

	Reservation baseReservation;

	Date testStart = new Date();
	Date baseReservationStart;
	Date baseReservationEnd;

	@Before
	public void setUp() throws Exception {
		baseReservation = new Reservation();
		Calendar start = Calendar.getInstance();
		start.setTime(testStart);

		start.set(Calendar.MINUTE, (start.get(Calendar.MINUTE) / 15) * 15);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		start.add(Calendar.MINUTE, 15);

		baseReservationStart = start.getTime();
		baseReservationEnd = DateTimeUtil.advance(baseReservationStart, Calendar.MINUTE, 30);
		baseReservation.setFirstName("Ford");
		baseReservation.setLastName("Prefect");
		baseReservation.setAddress("Café Lou");
		baseReservation.setCity("Gretchen Town");
		baseReservation.setPostalCode("New Betel");
		baseReservation.setStartTime(baseReservationStart);
		baseReservation.setEndTime(baseReservationEnd);
	}

	@Test
	public void create() {
		Reservation copy = new Reservation(baseReservation);
		Reservation r = reservationRepository.saveAndCheck(copy);
		assertNotNull(r);
		assertNotNull(r.getId());
		assertTrue(testStart.getTime() < r.getCreatedDate().getTime());
		assertTrue(testStart.getTime() < r.getModifiedDate().getTime());
		assertEquals("Ford", r.getFirstName());
		assertEquals("Prefect", r.getLastName());
		assertEquals("Café Lou", r.getAddress());
		assertEquals("Gretchen Town", r.getCity());
		assertEquals("New Betel", r.getPostalCode());
		reservationRepository.delete(r);
	}

	@Test
	public void createBeforeAndAfter() {
		Reservation now = new Reservation(baseReservation);
		Reservation before = new Reservation(baseReservation);
		Reservation after = new Reservation(baseReservation);

		before.setStartTime(DateTimeUtil.advance(before.getStartTime(), Calendar.HOUR, -1));
		before.setEndTime(DateTimeUtil.advance(before.getEndTime(), Calendar.HOUR, -1));
		after.setStartTime(DateTimeUtil.advance(after.getStartTime(), Calendar.HOUR, 1));
		after.setEndTime(DateTimeUtil.advance(after.getEndTime(), Calendar.HOUR, 1));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nBefore = reservationRepository.saveAndCheck(before);
		Reservation nAfter = reservationRepository.saveAndCheck(after);

		assertNotNull(nNow);
		assertNotNull(nBefore);
		assertNotNull(nAfter);

		reservationRepository.delete(nNow);
		reservationRepository.delete(nBefore);
		reservationRepository.delete(nAfter);
	}

	@Test
	public void createOver() {
		Reservation now = new Reservation(baseReservation);
		Reservation over = new Reservation(baseReservation);

		over.setStartTime(DateTimeUtil.advance(over.getStartTime(), Calendar.HOUR, -1));
		over.setEndTime(DateTimeUtil.advance(over.getEndTime(), Calendar.HOUR, 1));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nOver = reservationRepository.saveAndCheck(over);

		assertNotNull(nNow);
		assertNull(nOver);

		reservationRepository.delete(nNow);
	}

	@Test
	public void createInside() {
		Reservation now = new Reservation(baseReservation);
		Reservation inside = new Reservation(baseReservation);

		inside.setStartTime(DateTimeUtil.advance(inside.getStartTime(), Calendar.MINUTE, 10));
		inside.setEndTime(DateTimeUtil.advance(inside.getEndTime(), Calendar.MINUTE, 20));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nInside = reservationRepository.saveAndCheck(inside);

		assertNotNull(nNow);
		assertNull(nInside);

		reservationRepository.delete(nNow);
	}

	@Test
	public void createBeforeOverlapped() {
		Reservation now = new Reservation(baseReservation);
		Reservation before = new Reservation(baseReservation);

		before.setStartTime(DateTimeUtil.advance(before.getStartTime(), Calendar.MINUTE, -10));
		before.setEndTime(DateTimeUtil.advance(before.getEndTime(), Calendar.MINUTE, 10));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nBefore = reservationRepository.saveAndCheck(before);

		assertNotNull(nNow);
		assertNull(nBefore);

		reservationRepository.delete(nNow);
	}

	@Test
	public void createAfterOverlapped() {
		Reservation now = new Reservation(baseReservation);
		Reservation after = new Reservation(baseReservation);

		after.setStartTime(DateTimeUtil.advance(after.getStartTime(), Calendar.MINUTE, 20));
		after.setEndTime(DateTimeUtil.advance(after.getEndTime(), Calendar.MINUTE, 40));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nAfter = reservationRepository.saveAndCheck(after);

		assertNotNull(nNow);
		assertNull(nAfter);

		reservationRepository.delete(nNow);
	}

	@Test
	public void createEndOnStart() {
		Reservation now = new Reservation(baseReservation);
		Reservation before = new Reservation(baseReservation);

		before.setStartTime(DateTimeUtil.advance(before.getStartTime(), Calendar.MINUTE, -30));
		before.setEndTime(DateTimeUtil.advance(before.getEndTime(), Calendar.MINUTE, -30));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nBefore = reservationRepository.saveAndCheck(before);

		assertNotNull(nNow);
		assertNotNull(nBefore);

		reservationRepository.delete(nNow);
		reservationRepository.delete(nBefore);
	}

	@Test
	public void createStartOnEnd() {
		Reservation now = new Reservation(baseReservation);
		Reservation after = new Reservation(baseReservation);

		after.setStartTime(DateTimeUtil.advance(after.getStartTime(), Calendar.MINUTE, 30));
		after.setEndTime(DateTimeUtil.advance(after.getEndTime(), Calendar.MINUTE, 1));
		Reservation nNow = reservationRepository.saveAndCheck(now);
		Reservation nAfter = reservationRepository.saveAndCheck(after);

		assertNotNull(nNow);
		assertNotNull(nAfter);

		reservationRepository.delete(nNow);
		reservationRepository.delete(nAfter);
	}

	@Test
	public void slots() {
		Calendar next = Calendar.getInstance();
		next.setTime(new Date());

		next.set(Calendar.MINUTE, (next.get(Calendar.MINUTE) / 15) * 15);
		next.set(Calendar.SECOND, 0);
		next.set(Calendar.MILLISECOND, 0);
		next.add(Calendar.MINUTE, 15);
		List<ReservationSlot> slots = reservationRepository.getFreeReservationSlots(next.getTime(), 15);

		next.add(Calendar.MINUTE, 15);

		for (ReservationSlot slot : slots) {
			assertEquals(next.getTime(),slot.getStartTime());
			next.add(Calendar.MINUTE, 15);
			assertEquals(next.getTime(),slot.getEndTime());
		}
	}
}