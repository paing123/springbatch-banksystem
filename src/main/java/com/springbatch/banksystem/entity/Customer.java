package com.springbatch.banksystem.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity
@Data
public class Customer{
	
	@Id
    @Column (name = "customer_id")
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long customerId;

    @NotNull
    @Column (name = "customer_name", nullable = false)
    private String customerName;

    @NotNull
    @Column (name = "dob", nullable = false)
    private Date dob;
    
    @NotNull
    @Column (name = "address", nullable = false)
    private String address;
    
    @OneToMany(fetch = FetchType.LAZY)    
    @JsonIgnore
    private List<Account> account;
    
    @OneToMany(fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;
    
}
