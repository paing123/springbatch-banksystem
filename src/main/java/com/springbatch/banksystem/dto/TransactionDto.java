package com.springbatch.banksystem.dto;

import com.springbatch.banksystem.entity.Account;
import com.springbatch.banksystem.entity.Customer;

import lombok.Data;

@Data
public class TransactionDto {

	private Long trxId;
	
	private String trxDate;
	
	private String trxTime;
	
	private Double trxAmount;
	
	private String description;
	
	private Long accountNumber;
	
	private Long customerId;
	
	private Account account;
	
	private Customer customer;
    
}
