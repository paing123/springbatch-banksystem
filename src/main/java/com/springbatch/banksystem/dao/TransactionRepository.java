package com.springbatch.banksystem.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springbatch.banksystem.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	
	@Query("SELECT t FROM Transaction t WHERE t.customer.customerId = ?1 OR t.account.accountNumber = ?2 "
			+ "OR LOWER(t.description) LIKE LOWER(CONCAT('%', ?3,'%')) ")
	Page<Transaction> findByCustomerIdOrAccountNumberOrDescription(Long customerId, Long accountNumber, String description,Pageable pageable);
}
