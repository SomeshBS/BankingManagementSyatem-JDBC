package com.virtual.bank.customexceptions;

public class InsufficientBalanceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4081539248662214045L;

	public InsufficientBalanceException(String message) {
		super(message);
		System.out.println("You dont have sufficient balance to perform the transaction");
	}
}
