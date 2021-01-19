package com.demo.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDateTime;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.utils.DateUtil;

/**
 * @author Aymen Chaaben
 *
 */
@SpringBootTest(classes = TestApplicationConfiguration.class)
public class AccountTest {

	@Test
	void contextLoads() {
	}

	/**
	 * test Create Simple Account
	 * 
	 */
	@Test
	void testCreateSimpleAccount() {
		Account myAccount = new Account();
		assertNotNull(myAccount);
	}

	/**
	 * test Create Account With Num Account
	 * 
	 */
	@Test
	void testCreateAccountWithNumAccount() {
		Account myAccount = new Account();
		assertNotNull(myAccount);
		assertNotNull(myAccount.getNumAccount());
	}

	/**
	 * test get Amount
	 * 
	 */
	@Test
	void testgetAmount() {
		Account myAccount = new Account();
		assertNotNull(myAccount);
		assertNotNull(myAccount.getNumAccount());
		assertEquals(0, myAccount.getBalance());
	}

	/**
	 * test Deposite Amount
	 * @throws OperationException
	 */
	@Test
	void testDepositeAmount() throws OperationException {
		Account myAccount = new Account();
		double amount = 1000;
		myAccount.deposite(amount);
		assertNotNull(myAccount);
		assertNotNull(myAccount.getNumAccount());
		assertEquals(amount, myAccount.getBalance());
	}
	
	/**
	 * test Deposite Amount And Get Statement
	 * @throws OperationException
	 */
	@Test
	void testDepositeAmountAndGetStatement() throws OperationException {
		Account myAccount = new Account();
		double depositeAmount=1000;
		String statement = myAccount.deposite(depositeAmount);
		assertNotNull(myAccount);
		assertNotNull(myAccount.getNumAccount());
		assertEquals(depositeAmount, myAccount.getBalance());
		assertNotNull(statement);
		LocalDateTime now = LocalDateTime.now();
		assertEquals(now.format(DateUtil.FORMAT)+"|"+depositeAmount+"|"+depositeAmount, statement);
	}
	
	/**
	 * test Deposite Amount And save State Of Operation
	 * @throws OperationException
	 */
	@Test
	void testDepositeAmountAndsaveStateOfOperation() throws OperationException {
		Account myAccount = new Account();
		assertEquals(null,myAccount.getOperations());
		double amount = 1000;
		myAccount.deposite(amount);
		assertNotNull(myAccount);
		assertNotNull(myAccount.getNumAccount());
		assertEquals(amount, myAccount.getBalance());
		assertNotNull(myAccount.getOperations());
		assertEquals(1,myAccount.getOperations().size());
		if(CollectionUtils.isNotEmpty(myAccount.getOperations())) {
			Operation depositOperation=myAccount.getOperations().get(0);
			assertEquals(Operation.TYPE_OPERATION_DEPOSIT,depositOperation.getTypeOperation());
			assertEquals(myAccount.getBalance(),depositOperation.getBalance());
			assertEquals(amount,depositOperation.getAmount());
			assertNotNull(depositOperation.getDate());
		}
	}


	/**
	 * test Deposite Negative Amount
	 * 
	 */
	@Test
	void testDepositeNegativeAmount() {
		Account myAccount = new Account();
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> myAccount.deposite(-1000));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.DEPOSITED_AMOUNT_SHOULD_BE_GREATER_THAN_0,OperationExceptionThatWasThrown.getMessage());
		assertEquals(null,myAccount.getOperations());
	}
	
	
	/**
	 * test With Drawal
	 * @throws OperationException
	 */
	@Test
	void testWithDrawal() throws OperationException {
		Account myAccount = new Account();
		double depositeAmount=1000;
		double amountToWithdraw=-100;
		myAccount.deposite(depositeAmount);
		myAccount.withDrawal(amountToWithdraw);
		assertEquals(900, myAccount.getBalance());
		
	}
	/**
	 * test With Drawal And Get Statement
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalAndGetStatement() throws OperationException {
		Account myAccount = new Account();
		double depositeAmount=1000;
		double amountToWithdraw=-100;
		myAccount.deposite(depositeAmount);
		String statement=myAccount.withDrawal(amountToWithdraw);
		assertEquals(900.0, myAccount.getBalance());
		assertNotNull(statement);
		LocalDateTime now = LocalDateTime.now();
		assertEquals(now.format(DateUtil.FORMAT)+"|"+amountToWithdraw+"|"+900.0, statement);
	}
	
	/**
	 * test With Drawal And save State Of Operation
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalAndsaveStateOfOperation() throws OperationException {
		Account myAccount = new Account();
		assertEquals(null,myAccount.getOperations());
		double depositeAmount=1000;
		double amountToWithdraw=-100;
		myAccount.deposite(depositeAmount);
		myAccount.withDrawal(amountToWithdraw);
		assertEquals(900.0, myAccount.getBalance());
		assertNotNull(myAccount.getOperations());
		assertEquals(2,myAccount.getOperations().size());
		if(CollectionUtils.isNotEmpty(myAccount.getOperations())) {
			Operation withDrawalOperation=myAccount.getOperations().get(1);
			assertEquals(Operation.TYPE_OPERATION_WITHDRAWL,withDrawalOperation.getTypeOperation());
			assertEquals(myAccount.getBalance(),withDrawalOperation.getBalance());
			assertEquals(amountToWithdraw,withDrawalOperation.getAmount());
			assertNotNull(withDrawalOperation.getDate());
		}
	}


	/**
	 * test With Drawal Positive Amount
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalPositiveAmount() throws OperationException {
		Account myAccount = new Account();
		double amountToWithdraw=100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> myAccount.withDrawal(amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.WITH_DRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_0,OperationExceptionThatWasThrown.getMessage());
		assertEquals(null,myAccount.getOperations());
		
	}
	/**
	 * test With Drawal With No Money In Account
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalWithNoMoneyInAccount() throws OperationException {
		Account myAccount = new Account();
		double amountToWithdraw=-100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> myAccount.withDrawal(amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.NO_ENOUGH_AMOUNT_IN_YOUR_ACCOUNT,OperationExceptionThatWasThrown.getMessage());
		assertEquals(null,myAccount.getOperations());
		
	}
	/**
	 * test With Drawal With Amount Towithdraw Greater Than Balance
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalWithAmountTowithdrawGreaterThanBalance() throws OperationException {
		Account myAccount = new Account();
		myAccount.deposite(50);
		double amountToWithdraw=-100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> myAccount.withDrawal(amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.AMOUNT_CAN_BE_WITHDRAWN+myAccount.getBalance(),OperationExceptionThatWasThrown.getMessage());
		
	}
	
	
	
	
}
