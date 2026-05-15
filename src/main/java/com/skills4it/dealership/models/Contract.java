package com.skills4it.dealership.models;

import java.time.LocalDate;

public abstract class Contract {

	private LocalDate contractDate;
	private String customerName;
	private String customerEmail;
	private Vehicle vehicleSold;

	public Contract(LocalDate contractDate,
					String customerName,
					String customerEmail,
					Vehicle vehicleSold) {

		this.contractDate = contractDate;
		this.customerName = customerName;
		this.customerEmail = customerEmail;
		this.vehicleSold = vehicleSold;
	}

	public LocalDate getContractDate() {
		return contractDate;
	}

	public void setContractDate(LocalDate contractDate) {
		this.contractDate = contractDate;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Vehicle getVehicleSold() {
		return vehicleSold;
	}

	public void setVehicleSold(Vehicle vehicleSold) {
		this.vehicleSold = vehicleSold;
	}

	public abstract double getTotalPrice();

	public abstract double getMonthlyPayment();
}