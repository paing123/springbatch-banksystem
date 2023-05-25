package com.springbatch.banksystem.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Transaction{
			
	@Id
    @Column (name = "trx_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long trxId;
	
	@NotNull
    @Column (name = "trx_date", nullable = false)
	private String trxDate;
	
	@NotNull
    @Column (name = "trx_time", nullable = false)
	private String trxTime;
	
	@NotNull
    @Column (name = "trx_amount", nullable = false)
	private Double trxAmount;
	
	@NotNull
    @Column (name = "description", nullable = false)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "account_number", nullable = false)
	private Account account;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
//	@ManyToOne(
//			cascade= CascadeType.ALL
//			)
//	@JoinColumn(name="account_number")
//	private Account account;
//	
//	@ManyToOne(
//			cascade= CascadeType.ALL
//			)
//	@JoinColumn(name="customer_id")
//	private Customer customer;
	
//	public Long getAccountNumber() {
//		return account.getAccountNumber();
//	}
//	
//	public Long getCustomerId() {
//		return customer.getCustomerId();
//	}
}
