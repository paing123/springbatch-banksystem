package com.springbatch.banksystem.service;

import java.util.Optional;

import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.model.TransactionModel;


public interface TransactionService{
		
	public TransactionModel getAllTransaction(int pageNo, int pageSize);
	
	public Optional<Transaction> findById(Long id);
	
	public Transaction updateTransaction(Transaction transaction);
	
	public TransactionModel getTransactionByCusIdOrAcNumOrDescription(Transaction transaction, int pageNo, int pageSize);
}
