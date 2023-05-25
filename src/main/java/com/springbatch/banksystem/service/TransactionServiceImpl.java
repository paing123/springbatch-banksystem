package com.springbatch.banksystem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.springbatch.banksystem.dao.TransactionRepository;
import com.springbatch.banksystem.dto.TransactionDto;
import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.model.TransactionModel;

@Service
public class TransactionServiceImpl implements TransactionService{
	
	@Autowired
	private TransactionRepository transactionRepo;
	
	private Queue<Transaction> queue = new ConcurrentLinkedQueue<>(); // use a concurrent queue for thread-safety

	private ExecutorService executorService = Executors.newFixedThreadPool(10); // create a thread pool with 10 threads

	public void addToQueue(Transaction transaction) {
		queue.offer(transaction); // add the entity to the queue
	}

	@PostConstruct
	public void startProcessing() {
		executorService.submit(() -> {
			while (true) {
				Transaction entity = queue.poll(); // get the next entity from the queue
				if (entity != null) {
					updateTransaction(entity); // process the entity
				} else {
					try {
						Thread.sleep(1000); // wait for 1 second if the queue is empty
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
		});
	}
	
	@Override
	public TransactionModel getAllTransaction(int pageNo, int pageSize) {
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Transaction> transactions = transactionRepo.findAll(pageable);
		TransactionModel transModel = mapToModel(transactions, pageNo, pageSize);
		
		return transModel;
	}

	@Override
	public Optional<Transaction> findById(Long id) {
		return transactionRepo.findById(id);
	}

	@Override
	public Transaction updateTransaction(Transaction transaction) {
		return transactionRepo.save(transaction);
	}

	@Override
	public TransactionModel getTransactionByCusIdOrAcNumOrDescription(Transaction transaction,int pageNo, int pageSize) {
		
		Long customerId = 0l;
		Long accountNumber = 0l;
		if(transaction.getCustomer() != null) customerId = transaction.getCustomer().getCustomerId();
		if(transaction.getAccount() != null) accountNumber = transaction.getAccount().getAccountNumber();
		
		Pageable pageable = PageRequest.of(pageNo, pageSize);
		Page<Transaction> transactions = transactionRepo.findByCustomerIdOrAccountNumberOrDescription(customerId, accountNumber, 
																										transaction.getDescription(), pageable);
		TransactionModel transModel = mapToModel(transactions, pageNo, pageSize);
		
		return transModel;
	}
	
	private TransactionModel mapToModel(Page<Transaction> transactions, int pageNo, int pageSize) {
		
		List<Transaction> transactionList= transactions.stream().collect(Collectors.toList());
		
		TransactionModel transModel = new TransactionModel();
		transModel.setPageNo(pageNo);
		transModel.setPageSize(pageSize);
		transModel.setTotalElements(transactions.getTotalElements());
		transModel.setTotalPages(transactions.getTotalPages());
		transModel.setTransactionList(transactionList);
		transModel.setLast(transactions.isLast());
		
		return transModel;
	}
}
