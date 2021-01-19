package com.demo.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.repository.AccountRepository;
import com.demo.Bank.sevice.AccountService;
import com.demo.Bank.sevice.impl.AccountServiceImpl;
import com.demo.Bank.utils.DateUtil;

/**
 * @author Aymen Chaaben
 *
 */
@SpringBootTest(classes = TestApplicationConfiguration.class)
class AccountServiceTest {
	@Mock
	private AccountRepository accountRepository;

	@InjectMocks
	private AccountService accountService = new AccountServiceImpl();
	
	private final String numAccountExist = UUID.randomUUID().toString();
	private final String numAccountNotExist = UUID.randomUUID().toString();

	/**
	 * setMockOutput
	 * @throws OperationException
	 */
	@BeforeEach
	 void setMockOutput() throws OperationException {
	        when(accountRepository.loadAccountByNum(numAccountExist)).thenReturn(new Account());
	        when(accountRepository.loadAccountByNum(numAccountNotExist)).thenThrow( new OperationException(OperationException.ACCOUNT_NOT_FOUND));
	        when(accountRepository.updateAccount(Mockito.any(Account.class))).thenAnswer(new Answer() {
	            public Object answer(InvocationOnMock invocation) {
	                return invocation.getArguments()[0];
	            }
	        });
	 }

	@Test
	void contextLoads() {
	}
	
	/**
	 * test Deposite With Account Not Found
	 * @throws OperationException
	 */
	@Test
	void testDepositeWithAccountNotFound() throws OperationException {
		double amount=1000;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.deposite(numAccountNotExist, amount));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals("Operation Failed ==> Account not found",OperationExceptionThatWasThrown.getMessage());
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountNotExist);
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(0)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test Deposite Amount
	 * @throws OperationException
	 */
	@Test
	void testDepositeAmount() throws OperationException {
		double amount=1000;
		String statement= accountService.deposite(numAccountExist, amount);
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountExist);
		LocalDateTime now = LocalDateTime.now();
		assertEquals(now.format(DateUtil.FORMAT)+"|"+amount+"|"+amount, statement);
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository,Mockito.times(1)).updateAccount(boundedAccount.capture());
		double balance = boundedAccount.getValue().getBalance();
		List<Operation> operations = boundedAccount.getValue().getOperations();
		assertEquals(amount, balance);
		assertNotNull(operations);
		if(CollectionUtils.isNotEmpty(operations)) {
			Operation depositOperation=operations.get(0);
			assertEquals(Operation.TYPE_OPERATION_DEPOSIT,depositOperation.getTypeOperation());
			assertEquals(balance,depositOperation.getBalance());
			assertEquals(amount,depositOperation.getAmount());
			assertNotNull(depositOperation.getDate());
		}
	
	}
	

	/**
	 * test Deposite Negative Amount
	 * @throws OperationException
	 */
	@Test
	void testDepositeNegativeAmount() throws OperationException {
		double amount=-1000;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.deposite(numAccountExist, amount));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.DEPOSITED_AMOUNT_SHOULD_BE_GREATER_THAN_0,OperationExceptionThatWasThrown.getMessage());
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountExist);
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(0)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test With Drawal With Account Not Found
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalWithAccountNotFound() throws OperationException {
		double amount=-100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.withDrawal(numAccountNotExist, amount));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.ACCOUNT_NOT_FOUND,OperationExceptionThatWasThrown.getMessage());
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountNotExist);
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(0)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test With Drawal
	 * @throws OperationException
	 */
	@Test
	void testWithDrawal() throws OperationException {
		double depositeAmount=1000;
		double amountToWithdraw=-100;
		accountService.deposite(numAccountExist, depositeAmount);
		String statement= accountService.withDrawal(numAccountExist, amountToWithdraw);
		Mockito.verify(accountRepository, Mockito.times(2)).loadAccountByNum(numAccountExist);
		LocalDateTime now = LocalDateTime.now();
		assertEquals(now.format(DateUtil.FORMAT)+"|"+amountToWithdraw+"|"+900.0, statement);
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository,Mockito.times(2)).updateAccount(boundedAccount.capture());
		double balance = boundedAccount.getValue().getBalance();
		List<Operation> operations = boundedAccount.getValue().getOperations();
		assertEquals(900.0, balance);
		assertNotNull(operations);
		if(CollectionUtils.isNotEmpty(operations)) {
			Operation withDrawalOperation=operations.get(1);
			assertEquals(Operation.TYPE_OPERATION_WITHDRAWL,withDrawalOperation.getTypeOperation());
			assertEquals(balance,withDrawalOperation.getBalance());
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
		double amountToWithdraw=100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.withDrawal(numAccountExist, amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.WITH_DRAWAL_AMOUNT_SHOULD_BE_LESS_THAN_0,OperationExceptionThatWasThrown.getMessage());
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountExist);
		Mockito.verify(accountRepository, Mockito.times(0)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test With Drawal With No Amount In Account
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalWithNoAmountInAccount() throws OperationException {
		double amountToWithdraw=-100;
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.withDrawal(numAccountExist, amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.NO_ENOUGH_AMOUNT_IN_YOUR_ACCOUNT,OperationExceptionThatWasThrown.getMessage());
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountExist);
		Mockito.verify(accountRepository, Mockito.times(0)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test With Drawal With Amount Towithdraw Greater Than Balance
	 * @throws OperationException
	 */
	@Test
	void testWithDrawalWithAmountTowithdrawGreaterThanBalance() throws OperationException {
		double depositeAmount=70;
		double amountToWithdraw=-100;
		accountService.deposite(numAccountExist, depositeAmount);
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.withDrawal(numAccountExist, amountToWithdraw));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.AMOUNT_CAN_BE_WITHDRAWN+depositeAmount,OperationExceptionThatWasThrown.getMessage());
		ArgumentCaptor<Account> boundedAccount = ArgumentCaptor.forClass(Account.class);
		Mockito.verify(accountRepository, Mockito.times(2)).loadAccountByNum(numAccountExist);
		Mockito.verify(accountRepository, Mockito.times(1)).updateAccount(boundedAccount.capture());
	}
	
	/**
	 * test Get History Of Operations With Account Not Found
	 * @throws OperationException
	 */
	@Test
	void testGetHistoryOfOperationsWithAccountNotFound() throws OperationException {
		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountService.getHistoryOfOperations(numAccountNotExist));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.ACCOUNT_NOT_FOUND,OperationExceptionThatWasThrown.getMessage());
		Mockito.verify(accountRepository, Mockito.times(1)).loadAccountByNum(numAccountNotExist);
	}
	/**
	 * test Get History Of Operations
	 * @throws OperationException
	 */
	@Test
	void testGetHistoryOfOperations() throws OperationException {
		double depositeAmount=200;
		double amountToWithdraw=-100;
		accountService.deposite(numAccountExist, depositeAmount);
		accountService.withDrawal(numAccountExist, amountToWithdraw);
		List<Operation> historyOfOperation= accountService.getHistoryOfOperations(numAccountExist);
		assertNotNull(historyOfOperation);
		assertEquals(2,historyOfOperation.size());
		
		if(CollectionUtils.isNotEmpty(historyOfOperation)) {
			Operation depositOperation=historyOfOperation.get(0);
			assertEquals(Operation.TYPE_OPERATION_DEPOSIT,depositOperation.getTypeOperation());
			assertEquals(200,depositOperation.getBalance());
			assertEquals(200,depositOperation.getAmount());
			assertNotNull(depositOperation.getDate());
			
			Operation withDrawalOperation=historyOfOperation.get(1);
			assertEquals(Operation.TYPE_OPERATION_WITHDRAWL,withDrawalOperation.getTypeOperation());
			assertEquals(100,withDrawalOperation.getBalance());
			assertEquals(amountToWithdraw,withDrawalOperation.getAmount());
			assertNotNull(withDrawalOperation.getDate());
		}
		
	}

}
