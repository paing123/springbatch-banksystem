package com.springbatch.banksystem.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springbatch.banksystem.entity.Transaction;
import com.springbatch.banksystem.exception.TransactionNotFoundException;
import com.springbatch.banksystem.model.TransactionModel;
import com.springbatch.banksystem.service.TransactionService;
import com.springbatch.banksystem.util.AppConstants;

@RestController
@RequestMapping("/transactionapi")
public class TransactionRestController {
	
	@Autowired
	private TransactionService transactionService;
		
	@GetMapping(value="/alltransactions")
    public ResponseEntity<TransactionModel> getAllTransactions(@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, 
    																			required = false) int pageNo,
            													@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, 
            																	required = false) int pageSize){
		TransactionModel transModel = transactionService.getAllTransaction(pageNo, pageSize);
		if(transModel.getTransactionList().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(transModel, HttpStatus.OK);
    } 
		
	@PutMapping(value="/updatetransaction/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable("id") long id, @RequestBody Transaction updTransaction){
		Transaction transaction = transactionService.findById(id)
													.orElseThrow(() -> new TransactionNotFoundException("Not found Transaction with id = " + id));
		transaction.setDescription(updTransaction.getDescription());
		return new ResponseEntity<>(transactionService.updateTransaction(transaction), HttpStatus.OK);
    } 
	
	@GetMapping(value="/transactions")
    public ResponseEntity<TransactionModel> getTransactions(@RequestBody Transaction transaction,
												    		@RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, 
												    						required = false) int pageNo,
															@RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, 
																			required = false) int pageSize){
		TransactionModel transModel = transactionService.getTransactionByCusIdOrAcNumOrDescription(transaction, pageNo, pageSize);
		if(transModel.getTransactionList().isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(transModel, HttpStatus.OK);
    } 
}
