package com.demo.Bank.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "operation")
public class Operation {
	public static final String TYPE_OPERATION_DEPOSIT = "DEPOSIT";
	public static final String TYPE_OPERATION_WITHDRAWL = "WITHDRAWL";
	
	@Id
	private String numOperation;
	private String typeOperation;
	private LocalDateTime date;
	private double amount;
	private double balance;
	@ManyToOne(fetch = FetchType.LAZY,optional = true)
	@JoinColumn(name="num_account",nullable = true)
	private Account account;

	public Operation() {
		super();
	}

	public Operation(String typeOperation, LocalDateTime date, double amount, double balance) {
		super();
		this.numOperation=UUID.randomUUID().toString(); 
		this.typeOperation = typeOperation;
		this.date = date;
		this.amount = amount;
		this.balance = balance;
	}

	public String getTypeOperation() {
		return typeOperation;
	}

	public void setTypeOperation(String typeOperation) {
		this.typeOperation = typeOperation;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getNumOperation() {
		return numOperation;
	}

	public void setNumOperation(String numOperation) {
		this.numOperation = numOperation;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	

}
