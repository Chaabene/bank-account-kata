package com.demo.Bank.Exception;

/**
 * @author Aymen Chaaben
 *
 */
public class OperationException extends Exception {

	public static final String AMOUNT_CAN_BE_WITHDRAWN = "Operation Failed ==> you can withdraw that : ";
	public static final String NO_ENOUGH_AMOUNT_IN_YOUR_ACCOUNT = "Operation Failed ==> you don't have enough money in your account";
	public static final String WITH_DRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_0 = "Operation Failed ==> withDrawal Amount should be less than 0";
	public static final String DEPOSITED_AMOUNT_SHOULD_BE_GREATER_THAN_0 = "Operation Failed ==> Deposited Amount should be greater than 0";
	public static final String ACCOUNT_NOT_FOUND = "Operation Failed ==> Account not found";
	public static final String NO_TRANSACTION = "No transaction performed on this account";
	public OperationException(String message) {
		super(message);
	}

	
}
