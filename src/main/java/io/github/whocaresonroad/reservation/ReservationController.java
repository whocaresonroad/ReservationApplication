package io.github.whocaresonroad.reservation;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/*
 * Controller for mapping Rest addresses
 */
@RestController
public class ReservationController {

	private final ReservationRepository reservationRepository;

	ReservationController(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	/*
	 * Get all reservations.
	 */
    @RequestMapping("/reservations")
    public List<Reservation> reservations() {
        return reservationRepository.findAll(new Sort(Sort.Direction.ASC, "startTime"));
    }

    /*
     * Get single reservation by id.
     */
    @RequestMapping("/reservation")
    public Reservation reservation(@RequestParam("id") String id) {
        return reservationRepository.findOne(id);
    }

    @RequestMapping("/reservations/reserve")
    public String reservations_reserve() {
    	// TODO: Create new reservation
        return "Hello from ReservationController reservations/reserve";
    }
}