package com.springbatch.banksystem.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

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
    
    @OneToOne(mappedBy = "customer")
    @JsonIgnore
    private Account account;
    
}
