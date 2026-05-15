package com.skills4it.dealership.models;

public abstract class Contract {
 private String contractDate;

 private String customerName;
 private String customerEmail;
 private boolean isVehicleSold;

 private Vehicle theVehicle;

 private double totalprice;
 private double monthlyPayment;

	public String getContractDate() {
		return contractDate;
	}

	public void setContractDate(String contractDate) {
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

	public boolean isVehicleSold() {
		return isVehicleSold;
	}

	public void setVehicleSold(boolean vehicleSold) {
		isVehicleSold = vehicleSold;
	}

	public abstract double getTotalprice();

	public abstract double setTotalprice();

}


