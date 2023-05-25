package com.springbatch.banksystem.job;

import java.util.HashSet;
import java.util.Set;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.BeanUtils;

import com.springbatch.banksystem.dto.TransactionDto;
import com.springbatch.banksystem.entity.Account;
import com.springbatch.banksystem.entity.Customer;
import com.springbatch.banksystem.entity.Transaction;

public class TransactionFilterProfessor implements ItemProcessor<TransactionDto, Transaction>{

	private Set<TransactionDto> transSet = new HashSet<TransactionDto>();
	private Transaction transEntity = new Transaction();
	private Account account = new Account();
	private Customer customer = new Customer();
	
	@Override
	public Transaction process(TransactionDto trans) throws Exception {
		
		if(transSet.contains(trans)) return null;
		transSet.add(trans);
		
		BeanUtils.copyProperties(trans, transEntity);
   	    account.setAccountNumber(trans.getAccountNumber());
   	    customer.setCustomerId(trans.getCustomerId());
   	    transEntity.setAccount(account);
   	    transEntity.setCustomer(customer);
		return transEntity;
	}
	
}
