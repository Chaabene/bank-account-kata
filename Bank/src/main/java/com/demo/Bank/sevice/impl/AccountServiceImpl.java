package com.demo.Bank.sevice.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;
import com.demo.Bank.domain.Operation;
import com.demo.Bank.repository.AccountRepository;
import com.demo.Bank.sevice.AccountService;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	@Autowired
	AccountRepository accountRepository;

	@Override
	public String deposite(String numAccount, double amount) throws OperationException {
		Account account = accountRepository.loadAccountByNum(numAccount);
		String Statement= account.deposite(amount);
		accountRepository.updateAccount(account);
		return Statement;
	}

	@Override
	public String withDrawal(String numAccount, double amountToWithdraw) throws OperationException {
		Account account = accountRepository.loadAccountByNum(numAccount);
		String Statement= account.withDrawal(amountToWithdraw);
		accountRepository.updateAccount(account);
		return Statement;
	}

	@Override
	public List<Operation> getHistoryOfOperations(String numAccount) throws OperationException {
		Account account = accountRepository.loadAccountByNum(numAccount);
		return account.getOperations();
	}

}
