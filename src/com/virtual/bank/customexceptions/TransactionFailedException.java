package com.virtual.bank.customexceptions;

public class TransactionFailedException extends Throwable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7352008625878943966L;

	public TransactionFailedException(String message) {
		super(message);
	}
	
}
