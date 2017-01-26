package io.github.whocaresonroad.reservation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	ReservationConfigurationTest.class,
	ReservationLenghtItemTest.class,
	ReservationRepositoryTest.class,
	ReservationRepresentationTest.class,
	ReservationSlotTest.class,
	ReservationTest.class,
	SpringElementsTest.class
	 })

public class ReservationApplicationTestSuite {
}
