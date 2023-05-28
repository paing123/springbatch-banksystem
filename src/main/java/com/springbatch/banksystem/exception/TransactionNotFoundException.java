package com.springbatch.banksystem.exception;

public class TransactionNotFoundException extends RuntimeException{

	private static final long serialVersionUID = -5579451727056233458L;

	public TransactionNotFoundException(String message) {
		super(message);
	}
}
