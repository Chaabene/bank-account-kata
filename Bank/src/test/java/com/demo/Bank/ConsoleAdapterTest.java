package com.demo.Bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.out.put.adapter.ConsoleAdapter;
import com.demo.Bank.utils.DateUtil;

/**
 * @author Aymen Chaaben
 *
 */
@SpringBootTest(classes = TestApplicationConfiguration.class)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ConsoleAdapterTest {

	@Autowired
	private ConsoleAdapter consoleAdapter;
	
	private final String numAccountExist = "a64a5fe8-6249-425c-9ff1-5395562c11b3";
	private final String numAccountNotExist = "a64a5fe8-6249-425c-9ff1-5395562c11b2";
	private final String excpectedOption="type:1 : For Deposite2 : For Withdrawal3 : For history4 : Exit";
	private final PrintStream standardOut = System.out;
	private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
	private LocalDateTime now = LocalDateTime.now();
	

	/**
	 * setUp
	 * 
	 */
	@BeforeEach
	public void setUp() {
	    System.setOut(new PrintStream(outputStreamCaptor));
	}

	@Test
	void contextLoads() {
	}

	/**
	 * get Possible Option
	 * 
	 */
	@Test
	void getPossibleOption() {
		ByteArrayInputStream in = new ByteArrayInputStream("4".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		assertEquals(excpectedOption,result);
	
	}
	/**
	 * get Possible Option And Enter String
	 * 
	 */
	@Test
	void getPossibleOptionAndEnterString() {
		ByteArrayInputStream in = new ByteArrayInputStream("dddd\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("we don't have this option")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	
	}
	/**
	 * get Possible Option And Enter Not Exist Opt
	 * 
	 */
	@Test
	void getPossibleOptionAndEnterNotExistOpt() {
		ByteArrayInputStream in = new ByteArrayInputStream("9\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("we don't have this option")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	
	}
	
	
	/**
	 * test Execute Deposite Operation With Not Found Account
	 * 
	 */
	@Test
	void testExecuteDepositeOperationWithNotFoundAccount() {
		ByteArrayInputStream in = new ByteArrayInputStream("1\n1000\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountNotExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to save")
		.append(OperationException.ACCOUNT_NOT_FOUND)
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	/**
	 * test Execute Deposite Operation
	 * 
	 */
	@Test
	void testExecuteDepositeOperation() {
		ByteArrayInputStream in = new ByteArrayInputStream("1\n1000\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to save")
		.append(now.format(DateUtil.FORMAT)+"|1000.0|1000.0")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	/**
	 * test Execute Deposite Operation With String Amount
	 * 
	 */
	@Test
	void testExecuteDepositeOperationWithStringAmount() {
		ByteArrayInputStream in = new ByteArrayInputStream("1\nhhhh\n1000\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to save")
		.append("You must enter a number")
		.append("You must enter a number")
		.append(now.format(DateUtil.FORMAT)+"|1000.0|1000.0")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	
	/**
	 * test Execute With Drawal Operation With Not Found Account
	 * 
	 */
	@Test
	void testExecuteWithDrawalOperationWithNotFoundAccount() {
		ByteArrayInputStream in = new ByteArrayInputStream("2\n-100\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountNotExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to get")
		.append(OperationException.ACCOUNT_NOT_FOUND)
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
		
	}
	/**
	 * test Execute With Drawal Operation With No Money In Account
	 * 
	 */
	@Test
	void testExecuteWithDrawalOperationWithNoMoneyInAccount() {
		ByteArrayInputStream in = new ByteArrayInputStream("2\n-100\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to get")
		.append(OperationException.NO_ENOUGH_AMOUNT_IN_YOUR_ACCOUNT)
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	/**
	 * test Execute With Drawal Operation With Amount Towithdraw Greater Than Balance
	 * 
	 */
	@Test
	void testExecuteWithDrawalOperationWithAmountTowithdrawGreaterThanBalance() {
		ByteArrayInputStream in = new ByteArrayInputStream("2\n-100\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation("a64a5fe8-6249-425c-9ff1-5395562c11b0");
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to get")
		.append(OperationException.AMOUNT_CAN_BE_WITHDRAWN+"50.0")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	
	/**
	 * test Execute With Drawal Operation
	 * 
	 */
	@Test
	void testExecuteWithDrawalOperation() {
		ByteArrayInputStream in = new ByteArrayInputStream("2\n-30\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation("a64a5fe8-6249-425c-9ff1-5395562c11b0");
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to get")
		.append(now.format(DateUtil.FORMAT))
		.append("|-30.0|20.0")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	/**
	 * test Execute Load History Operation With Not Found Account
	 * 
	 */
	@Test
	void testExecuteLoadHistoryOperationWithNotFoundAccount() {
		ByteArrayInputStream in = new ByteArrayInputStream("3\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountNotExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append(OperationException.ACCOUNT_NOT_FOUND)
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	
	/**
	 * test Execute Load History Operation With No Operation
	 * 
	 */
	@Test
	void testExecuteLoadHistoryOperationWithNoOperation() {
		ByteArrayInputStream in = new ByteArrayInputStream("3\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append(OperationException.NO_TRANSACTION)
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
	}
	/**
	 * test Execute Load History Operation
	 * 
	 */
	@Test
	void testExecuteLoadHistoryOperation() {
		ByteArrayInputStream in = new ByteArrayInputStream("1\n1000\n2\n-200\n3\n4\n".getBytes());
		System.setIn(in);
		consoleAdapter.EnterOperation(numAccountExist);
		String result = outputStreamCaptor.toString().trim().replaceAll("\r", "").replaceAll("\n", "");
		StringBuilder builder= new StringBuilder();
		builder.append(excpectedOption)
		.append("type an amount to save")
		.append(now.format(DateUtil.FORMAT)+"|1000.0|1000.0")
		.append(excpectedOption)
		.append("type an amount to get")
		.append(now.format(DateUtil.FORMAT)+"|-200.0|800.0")
		.append(excpectedOption)
		.append(Operation.TYPE_OPERATION_DEPOSIT)
		.append("|")
		.append(now.format(DateUtil.FORMAT))
		.append("|1000.0|1000.0")
		.append(Operation.TYPE_OPERATION_WITHDRAWL)
		.append("|")
		.append(now.format(DateUtil.FORMAT))
		.append("|-200.0|800.0")
		.append(excpectedOption);
		assertEquals(builder.toString(),result);
		
		
	}
	
	/**
	 * tearDown
	 * 
	 */
	@AfterEach
	public void tearDown() {
	    System.setOut(standardOut);
	}

}
