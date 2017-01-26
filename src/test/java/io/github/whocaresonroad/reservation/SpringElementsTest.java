package io.github.whocaresonroad.reservation;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringElementsTest {

	@Autowired
	ReservationApplication ra;

	@Autowired
	ReservationConfiguration rc;

	@Autowired
	ReservationErrorController ec;

	@Autowired
	ReservationController rco;

	@Autowired
	ReservationRepository rr;

	@Autowired
	ReservationRepositoryImpl rri;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void autowired() {
		assertNotNull(ra);
		assertNotNull(rc);
		assertNotNull(ec);
		assertNotNull(rco);
		assertNotNull(rr);
		assertNotNull(rri);
	}

}
