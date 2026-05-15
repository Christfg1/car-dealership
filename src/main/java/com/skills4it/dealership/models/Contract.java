package com.skills4it.dealership.models;

public abstract class Contract {
 private String contractDate;

 private String customerName;
 private String customerEmail;
 private boolean isVehicleSold;
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

	public double getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}

//	A Contract will hold information common to all contacts.  It should be an abstract
//	class as you can't create a generic contract.
//			• Date (as string) of contract
//• Customer name
//• Customer email
//• Vehicle sold
//• Total price
//• Monthly payment
//	Methods will include a constructor and getters and setters for all fields except total
//	price and monthly payment.
}
