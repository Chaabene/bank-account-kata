package com.demo.Bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.demo.Bank.out.put.adapter.ConsoleAdapter;

@SpringBootApplication
public class BankApplication  { 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApplicationContext ctx= SpringApplication.run(BankApplication.class, args);
		ConsoleAdapter consoleAdapter =(ConsoleAdapter) ctx.getBean("consoleAdapter");
		
		final String  numAccount="a64a5fe8-6249-425c-9ff1-5395562c11b7";
		consoleAdapter.EnterOperation(numAccount);
	}

	

}
