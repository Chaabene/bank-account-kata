package com.demo.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.repository.AccountRepository;

/**
 * @author Aymen Chaaben
 *
 */
@SpringBootTest(classes = TestApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class AccountRepositoryTest {
	@Autowired
	private AccountRepository accountRepository;

	@Test
	void contextLoads() {
	}

	/**
	 * test load Found Account By Num
	 * @throws OperationException
	 */
	@Test
	@Transactional
	void testloadFoundAccountByNum() throws OperationException {
		Account accout = accountRepository.loadAccountByNum("a64a5fe8-6249-425c-9ff1-5395562c11b3");
		assertNotNull(accout);
		assertEquals(0.0, accout.getBalance());
	}

	/**
	 * test load Not Found Account By Num
	 * @throws OperationException
	 */
	@Test
	@Transactional
	void testloadNotFoundAccountByNum() throws OperationException {

		Throwable OperationExceptionThatWasThrown = assertThrows(OperationException.class,
				() -> accountRepository.loadAccountByNum("a64a5fe8-6249-425c-9ff1-5395562c11b5"));
		assertNotNull(OperationExceptionThatWasThrown.getMessage());
		assertEquals(OperationException.ACCOUNT_NOT_FOUND, OperationExceptionThatWasThrown.getMessage());
	}

	/**
	 * test Update Account
	 * @throws OperationException
	 */
	@Test
	@Transactional
	void testUpdateAccount() throws OperationException {
		Account accout = accountRepository.loadAccountByNum("a64a5fe8-6249-425c-9ff1-5395562c11b3");
		accout.deposite(1000);
		accout.withDrawal(-80);
		accout = accountRepository.updateAccount(accout);
		assertEquals(920.0, accout.getBalance());
		List<Operation> operations = accout.getOperations();
		assertNotNull(operations);
		assertEquals(2, operations.size());
		if (CollectionUtils.isNotEmpty(operations)) {
			Operation depositOperation = operations.get(0);
			assertEquals(Operation.TYPE_OPERATION_DEPOSIT, depositOperation.getTypeOperation());
			assertEquals(1000.0, depositOperation.getBalance());
			assertEquals(1000.0, depositOperation.getAmount());
			assertNotNull(depositOperation.getDate());
			
			Operation withDrawalOperation = operations.get(1);
			assertEquals(Operation.TYPE_OPERATION_WITHDRAWL, withDrawalOperation.getTypeOperation());
			assertEquals(accout.getBalance(), withDrawalOperation.getBalance());
			assertEquals(-80.0, withDrawalOperation.getAmount());
			assertNotNull(withDrawalOperation.getDate());
		}
	}

}
