package domain;

import java.time.LocalDateTime;

public class FlexDrive {

	private int id;
	private int userId;
	private String username;
	private String userFullName;
	private String from;
	private String to;
	private double distance;
	private LocalDateTime bookedFor;
	private LocalDateTime bookedAt;
	private String addressFrom;
	private String addressTo;
	private int passengers;
	private int luggage;
	private int pram;
	private int childCarSeat;
	private int assistive;
	private String comment;
	private Price price;
	private Car car;
	private boolean approved;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public LocalDateTime getBookedFor() {
		return bookedFor;
	}

	public void setBookedFor(LocalDateTime bookedFor) {
		this.bookedFor = bookedFor;
	}

	public LocalDateTime getBookedAt() {
		return bookedAt;
	}

	public void setBookedAt(LocalDateTime bookedAt) {
		this.bookedAt = bookedAt;
	}

	public String getAddressFrom() {
		return addressFrom;
	}

	public void setAddressFrom(String addressFrom) {
		this.addressFrom = addressFrom;
	}

	public String getAddressTo() {
		return addressTo;
	}

	public void setAddressTo(String addressTo) {
		this.addressTo = addressTo;
	}

	public int getPassengers() {
		return passengers;
	}

	public void setPassengers(int passengers) {
		this.passengers = passengers;
	}

	public int getLuggage() {
		return luggage;
	}

	public void setLuggage(int luggage) {
		this.luggage = luggage;
	}

	public int getPram() {
		return pram;
	}

	public void setPram(int pram) {
		this.pram = pram;
	}

	public int getChildCarSeat() {
		return childCarSeat;
	}

	public void setChildCarSeat(int childCarSeat) {
		this.childCarSeat = childCarSeat;
	}

	public int getAssistive() {
		return assistive;
	}

	public void setAssistive(int assistive) {
		this.assistive = assistive;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}
