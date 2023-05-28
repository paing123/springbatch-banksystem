package com.springbatch.banksystem.model;

import java.util.List;

import com.springbatch.banksystem.entity.Transaction;

import lombok.Data;

@Data
public class TransactionModel {
	
    private Integer pageNo;
    
    private Integer pageSize;
    
    private Long totalElements;
    
    private Integer totalPages;
    
    private Boolean last;
    
    private List<Transaction> transactionList;
    
}
