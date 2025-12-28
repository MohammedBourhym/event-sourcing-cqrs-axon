package com.example.cqrs.query.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import com.example.cqrs.enums.OperationType;
import com.example.cqrs.events.AccountCreatedEvent;
import com.example.cqrs.events.AccountCreditedEvent;
import com.example.cqrs.events.AccountDebitedEvent;
import com.example.cqrs.events.AccountStatusUpdatedEvent;
import com.example.cqrs.query.entities.Account;
import com.example.cqrs.query.entities.AccountOperation;
import com.example.cqrs.query.repository.AccountRepository;
import com.example.cqrs.query.repository.OperationRepository;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class AccountEventHandler {

    private AccountRepository accountRepository;
    private OperationRepository operationRepository;

    public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
    }

    @EventHandler
    public void on(AccountCreatedEvent event,EventMessage eventMessage) {
        log.info("################# AccountCreatedEvent ################");
        Account account = Account.builder()
                .id(event.getAccountId())
                .balance(event.getInitBalace())
                .currency(event.getCurrency())
                .status(event.getStatus())
                .createdAt(eventMessage.getTimestamp())
                .build();
        accountRepository.save(account);
    }


    

    @EventHandler
    public void on(AccountStatusUpdatedEvent event, EventMessage eventMessage){
        log.info("################# AccountStatusUpdatedEvent ################");
        Account account = accountRepository.findById(event.accountId()).get();
        account.setStatus(event.toStatus());
        accountRepository.save(account);
    }


    @EventHandler
    public void on(AccountDebitedEvent event, EventMessage eventMessage){
        log.info("################# AccountDebitedEvent ################");
        Account account = accountRepository.findById(event.accountId()).get();
        AccountOperation operation = AccountOperation.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.amount())
                .type(OperationType.DEBIT)
                .account(account)
                .build();
        AccountOperation savedOperation = operationRepository.save(operation);
        account.setBalance(account.getBalance()-operation.getAmount());
        accountRepository.save(account);
    

    }
    @EventHandler
    public void on(AccountCreditedEvent event, EventMessage eventMessage){
        log.info("################# AccountCreditedEvent ################");
        Account account = accountRepository.findById(event.getAccountId()).get();
        AccountOperation operation = AccountOperation.builder()
                .date(eventMessage.getTimestamp())
                .amount(event.getAmount())
                .type(OperationType.CREDIT)
                .account(account)
                .build();
        AccountOperation savedOperation = operationRepository.save(operation);
        account.setBalance(account.getBalance()+operation.getAmount());
        accountRepository.save(account);
        

    }
}
