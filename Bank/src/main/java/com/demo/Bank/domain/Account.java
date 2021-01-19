package com.demo.Bank.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.collections4.CollectionUtils;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.utils.DateUtil;
/**
 * @author Aymen Chaaben
 *
 */
@Entity
@Table(name = "account")
public class Account {

	@Id
	private String numAccount;
	private double balance;
	@OneToMany(mappedBy = "account",cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
	private List<Operation> operations;

	public Account() {
		super();
		this.numAccount = UUID.randomUUID().toString();
	}

	public String getNumAccount() {
		return this.numAccount;
	}

	public double getBalance() {
		return this.balance;
	}

	public List<Operation> getOperations() {
		return operations;
	}

	public void setOperations(List<Operation> operations) {
		this.operations = operations;
	}

	public void setNumAccount(String numAccount) {
		this.numAccount = numAccount;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	/**
	 * deposite
	 * @param amount
	 * @return
	 * @throws OperationException
	 */
	public String deposite(double amount) throws OperationException {
		if (amount <= 0) {
			throw new OperationException(OperationException.DEPOSITED_AMOUNT_SHOULD_BE_GREATER_THAN_0);
		}
		return saveOperation(amount, Operation.TYPE_OPERATION_DEPOSIT);
	}

	/**
	 * withDrawal
	 * @param amount
	 * @return
	 * @throws OperationException
	 */
	public String withDrawal(double amount) throws OperationException {
		if (amount >= 0) {
			throw new OperationException(OperationException.WITH_DRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_0);
		}

		if (this.balance == 0) {
			throw new OperationException(OperationException.NO_ENOUGH_AMOUNT_IN_YOUR_ACCOUNT);
		}

		if (this.balance < Math.abs(amount)) {
			throw new OperationException(OperationException.AMOUNT_CAN_BE_WITHDRAWN + this.balance);
		}
		return saveOperation(amount,Operation.TYPE_OPERATION_WITHDRAWL);
	}

	/**
	 * save operation
	 * @param amount
	 * @param Operation
	 * @return
	 */
	private String saveOperation(double amount, String Operation) {
		String accountStatement;
		this.balance += amount;
		LocalDateTime now = LocalDateTime.now();
		accountStatement = now.format(DateUtil.FORMAT) + "|" + amount + "|" + this.balance;
		Operation operation = new Operation(Operation, now, amount, balance);
		if (CollectionUtils.isEmpty(operations)) {
			operations = new ArrayList<>();
		}
		operation.setAccount(this);
		operations.add(operation);
		return accountStatement;
	}

}
