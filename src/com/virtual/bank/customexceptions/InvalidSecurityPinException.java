package com.virtual.bank.customexceptions;

public class InvalidSecurityPinException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7352008625878943966L;

	public InvalidSecurityPinException(String message) {
		super(message);
		System.out.println("The Security PIN entered is invalid, please check");
	}
	
}
