package com.virtual.bank.model;

public class Account extends User {
	
	
	private long account_number;
	private double balance;
	private String security_pin;
	public long getAccount_number() {
		return account_number;
	}
	public void setAccount_number(long account_number) {
		this.account_number = account_number;
	}
	public double getbalance() {
		return balance;
	}
	public void setbalance(double balance) {
		this.balance = balance;
	}
	public String getSecurity_pin() {
		return security_pin;
	}
	public void setSecurity_pin(String security_pin) {
		this.security_pin = security_pin;
	}
	
	
	

}
