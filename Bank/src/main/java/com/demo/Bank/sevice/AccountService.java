package com.demo.Bank.sevice;

import java.util.List;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Operation;

public interface AccountService {

	/**
	 * deposite
	 * @param numAccount
	 * @param amount
	 * @return
	 * @throws OperationException
	 */
	String deposite(String numAccount, double amount)throws OperationException;

	/**
	 * withDrawal
	 * @param numAccount
	 * @param amountToWithdraw
	 * @return
	 * @throws OperationException
	 */
	String withDrawal(String numAccount, double amountToWithdraw) throws OperationException;

	/**
	 * get History Of Operations
	 * @param numAccount
	 * @return
	 * @throws OperationException
	 */
	List<Operation> getHistoryOfOperations(String numAccount)throws OperationException;


}
