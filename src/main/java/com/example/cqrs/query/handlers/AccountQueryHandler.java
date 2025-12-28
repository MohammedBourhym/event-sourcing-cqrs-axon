package com.example.cqrs.query.handlers;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

import com.example.cqrs.query.entities.Account;
import com.example.cqrs.query.queries.GetAllAccountsQuery;
import com.example.cqrs.query.repository.AccountRepository;
import com.example.cqrs.query.repository.OperationRepository;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AccountQueryHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountQueryHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }
    
    @QueryHandler
    public List<Account> on(GetAllAccountsQuery query){
        return accountRepository.findAll();
    }

}
