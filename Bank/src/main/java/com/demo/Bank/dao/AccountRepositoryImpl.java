package com.demo.Bank.dao;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;
import com.demo.Bank.repository.AccountRepository;

/**
 * @author Aymen Chaaben
 *
 */
@Repository
public class AccountRepositoryImpl implements AccountRepository {

	@Autowired
	EntityManager entityManager;

	@Override
	public Account loadAccountByNum(String numAccount) throws OperationException {
		  Account account = entityManager.find(Account.class, numAccount);
		  if(account == null) {
			throw new OperationException(OperationException.ACCOUNT_NOT_FOUND);
		  }
		  return account;
	}

	@Override
	public Account updateAccount(Account account) {
		return entityManager.merge(account);
	}

}
