package com.maveric.transactionservice;

import com.maveric.transactionservice.constant.AccountType;
import com.maveric.transactionservice.constant.Type;
import com.maveric.transactionservice.dto.AccountDto;
import com.maveric.transactionservice.dto.TransactionDto;
import com.maveric.transactionservice.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public
class TransactionServiceApplicationTests {

	@Test
	void contextLoads() {
		assertTrue(true);
	}

	public static Transaction getTransaction(){
		Transaction transaction = new Transaction();
		transaction.set_id("1234");
		transaction.setType(Type.CREDIT);
		transaction.setAccountId("1234");
		transaction.setAmount(872.32);
		transaction.setCreatedAt(LocalDateTime.now());
		return transaction;
	}

	public static Transaction getTransactionInvalidData(){
		Transaction transaction = new Transaction();
		transaction.set_id("1234");
		transaction.setType(Type.CREDIT);
		transaction.setAccountId("1234");
		transaction.setAmount(-1);
		transaction.setCreatedAt(LocalDateTime.now());
		return transaction;
	}

	public static TransactionDto getTransactionDtoInvalidData(){
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.set_id("1234");
		transactionDto.setType(Type.CREDIT);
		transactionDto.setAccountId("");
		transactionDto.setAmount(872.32);
		transactionDto.setCreatedAt(LocalDateTime.now());
		return transactionDto;
	}

	public static TransactionDto getTransactionDto()
	{
		TransactionDto transactionDto = new TransactionDto();
		transactionDto.set_id("1234");
		transactionDto.setType(Type.CREDIT);
		transactionDto.setAccountId("1234");
		transactionDto.setAmount(872.32);
		transactionDto.setCreatedAt(LocalDateTime.now());
		return transactionDto;
	}

	public static AccountDto getAccountDto(){
		AccountDto accountDto = new AccountDto();
		accountDto.set_id("1234");
		accountDto.setCustomerId("1234");
		accountDto.setType(AccountType.CURRENT);
		return accountDto;
	}

}
