package com.demo.Bank.out.put.adapter;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.sevice.AccountService;
import com.demo.Bank.utils.DateUtil;

@Component
public class ConsoleAdapter {
	@Autowired
	private AccountService accountService;

	/**
	 * Enter Operation 
	 * @param numAccount
	 */
	public void EnterOperation(final String numAccount) {
		boolean operationToDo = true;
		Scanner console = new Scanner(System.in);
		console.useLocale(Locale.US);
		while (operationToDo) {
			System.out.println("type:");
			System.out.println("1 : For Deposite");
			System.out.println("2 : For Withdrawal");
			System.out.println("3 : For history");
			System.out.println("4 : Exit");
				if (console.hasNextInt()) {
					int typeOperation = console.nextInt();
					switch (typeOperation) {
					case 1:
						System.out.println("type an amount to save");
						checkAndExcuteOperation(console, numAccount, typeOperation);
						break;
					case 2:
						System.out.println("type an amount to get");
						checkAndExcuteOperation(console, numAccount, typeOperation);
						break;
					case 3:
						executeOperation(typeOperation, numAccount, 0);
						break;
					case 4:
						operationToDo = false;
						console.close();
						break;
					default:
						operationNotExist(console);
						break;
					}
					
				} else {
					operationNotExist(console);
				}
			
		}
	}

	private void operationNotExist(Scanner console) {
		System.out.println("we don't have this option");
		console.nextLine();
	}

	/**
	 * check And Excute Operation
	 * @param console
	 * @param numAccount
	 * @param typeOperation
	 */
	private void checkAndExcuteOperation(Scanner console, final String numAccount, int typeOperation) {
		double amount = 0;
		boolean operationExcuted = false;
		while (!operationExcuted) {
			if (console.hasNextDouble()) {
				if (typeOperation == 1) {
					amount = console.nextDouble();
				} else {
					amount = -Math.abs(console.nextDouble());
				}
				executeOperation(typeOperation, numAccount, amount);
				operationExcuted = true;
			} else {
				System.out.println("You must enter a number");
				console.nextLine();
			}
		}
	}

	/**
	 * execute Operation
	 * @param indiceOperation
	 * @param numAccount
	 * @param amount
	 */
	private void executeOperation(int indiceOperation, String numAccount, double amount) {
		try {
			switch (indiceOperation) {
			case 1:
				System.out.println(accountService.deposite(numAccount, amount));
				break;
			case 2:
				System.out.println(accountService.withDrawal(numAccount, amount));
				break;
			case 3:
				loadHistory(numAccount);
				break;
			}
		} catch (OperationException e) {
			System.out.println(e.getMessage());
		}

	}

	/**
	 * load History
	 * @param numAccount
	 * @throws OperationException
	 */
	private void loadHistory(String numAccount) throws OperationException {
		List<Operation> historyOfOperations = accountService.getHistoryOfOperations(numAccount);
		if (CollectionUtils.isEmpty(historyOfOperations)) {
			System.out.println(OperationException.NO_TRANSACTION);
		} else {
			historyOfOperations.stream().forEach(op -> System.out.println(op.getTypeOperation() + "|"
					+ op.getDate().format(DateUtil.FORMAT) + "|" + op.getAmount() + "|" + op.getBalance()));
		}

	}

}
