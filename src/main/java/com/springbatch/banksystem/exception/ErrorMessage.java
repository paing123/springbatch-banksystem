package com.springbatch.banksystem.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorMessage {

	private int statusCode;

	private String timestamp;
	
	private String message;

	private String description;
}
