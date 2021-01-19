package com.demo.Bank.repository;

import com.demo.Bank.Exception.OperationException;
import com.demo.Bank.domain.Account;

public interface AccountRepository {

	Account loadAccountByNum(String numAccount)throws OperationException;;

	Account updateAccount(Account account);

}
