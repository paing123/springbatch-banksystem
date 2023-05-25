package com.springbatch.banksystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.springbatch.banksystem.entity.Customer;
import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.model.TransactionModel;


public interface TransactionService{
		
	public TransactionModel getAllTransaction(int pageNo, int pageSize);
	
	public Optional<Transaction> findById(Long id);
	
	public Transaction updateTransaction(Transaction transaction);
	
	public TransactionModel getTransactionByCusIdOrAcNumOrDescription(Transaction transaction, int pageNo, int pageSize);
}
