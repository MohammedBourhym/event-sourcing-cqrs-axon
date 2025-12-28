package com.example.cqrs.query.handlers;

import java.util.List;

import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Component;

import com.example.cqrs.commands.dto.AccountEvent;
import com.example.cqrs.commands.dto.AccountStatement;
import com.example.cqrs.query.entities.Account;
import com.example.cqrs.query.entities.AccountOperation;
import com.example.cqrs.query.queries.GetAccountStatementQuery;
import com.example.cqrs.query.queries.GetAllAccountsQuery;
import com.example.cqrs.query.queries.WatchEventQuery;
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
    public List<Account> on(GetAllAccountsQuery query) {
        return accountRepository.findAll();
    }

    @QueryHandler
    public AccountStatement on(GetAccountStatementQuery query) {
        Account account = accountRepository.findById(query.getAccountId()).get();
        List<AccountOperation> operations = operationRepository.findByAccountId(query.getAccountId());
        return new AccountStatement(account, operations);
    }

    @QueryHandler
    public AccountEvent on(WatchEventQuery query) {
        return AccountEvent.builder().build();
    }

}
