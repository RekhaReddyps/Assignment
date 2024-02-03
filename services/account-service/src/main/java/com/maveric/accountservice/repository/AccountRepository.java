package com.maveric.accountservice.repository;

import com.maveric.accountservice.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface AccountRepository extends MongoRepository<Account,String> {
        Page<Account> findByCustomerId(Pageable page, String userId);

        List<Account> findAccountsByCustomerId(String userId);

}

