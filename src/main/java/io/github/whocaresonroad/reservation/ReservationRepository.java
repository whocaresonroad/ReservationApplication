package io.github.whocaresonroad.reservation;

import org.springframework.data.mongodb.repository.MongoRepository;

/*
 * Repository interface for Spring, ReservationRepositoryCustom contains all the methods.
 */
public interface ReservationRepository extends MongoRepository<Reservation, String>, ReservationRepositoryCustom {
}