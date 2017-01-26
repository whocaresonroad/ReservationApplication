package io.github.whocaresonroad.reservation;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

/*
 * MongoDB document representation class for the actual reservation.
 * The fields id, createdDate and modifiedDate are managed by the db.
 * The collection name is Reservation.
 */
@Document(collection = "Reservation")
public class Reservation {
	@Id
	private String id;
	private String firstName = new String("");
	private String lastName = new String("");
	private String address = new String("");
	private String postalCode = new String("");
	private String city = new String("");
	private Date startTime = new Date();
	private Date endTime = new Date();
	@CreatedDate
	private Date createdDate;
	@LastModifiedDate
	private Date modifiedDate;

	public Reservation() {
	}

	public Reservation(Reservation reservation) {
		this.firstName = reservation.firstName;
		this.lastName = reservation.lastName;
		this.address = reservation.address;
		this.postalCode = reservation.postalCode;
		this.city = reservation.city;
		this.startTime = reservation.startTime;
		this.endTime = reservation.endTime;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}
}