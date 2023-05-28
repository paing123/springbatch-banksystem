package com.springbatch.banksystem.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Account{
		
	@Id
    @Column (name = "account_number")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long accountNumber;

    @NotNull
    @Column (name = "amount", nullable = false)
    private Double amount;
    
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "customer_id", nullable = false)
	@JsonIgnore
    private Customer customer;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;
    
}
