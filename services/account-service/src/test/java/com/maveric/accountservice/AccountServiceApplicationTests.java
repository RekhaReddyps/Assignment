package com.maveric.accountservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.accountservice.dto.AccountDto;
import com.maveric.accountservice.entity.Account;
import com.maveric.accountservice.enums.Type;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public
class AccountServiceApplicationTests {

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Test
	void testDoSomething() {  // Noncompliant
		assertTrue(true);

	}
	
	public static Account getAccount ()
	{
		return Account.builder()
				.customerId("1234")
				.type(Type.SAVINGS)
				.build();
	}
	public static AccountDto getAccountDto ()
	{
		return AccountDto.builder()
				.customerId("1234")
				.type(Type.CURRENT)
				.build();
	}
}



