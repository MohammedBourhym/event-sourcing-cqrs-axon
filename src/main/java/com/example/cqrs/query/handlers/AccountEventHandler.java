package com.example.cqrs.query.handlers;

import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import com.example.cqrs.commands.dto.AccountEvent;
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
        private QueryUpdateEmitter queryUpdateEmitter;

        public AccountEventHandler(AccountRepository accountRepository, OperationRepository operationRepository,
                        QueryUpdateEmitter queryUpdateEmitter) {
                this.accountRepository = accountRepository;
                this.operationRepository = operationRepository;
                this.queryUpdateEmitter = queryUpdateEmitter;
        }

        @EventHandler
        public void on(AccountCreatedEvent event, EventMessage eventMessage) {
                log.info("################# AccountCreatedEvent ################");
                Account account = Account.builder()
                                .id(event.getAccountId())
                                .balance(event.getInitialBalance())
                                .currency(event.getCurrency())
                                .status(event.getStatus())
                                .createdAt(eventMessage.getTimestamp())
                                .build();
                accountRepository.save(account);

                AccountEvent accountEvent = AccountEvent.builder()
                                .type(event.getClass().getSimpleName())
                                .accountId(account.getId())
                                .balance(account.getBalance())
                                .amount(event.getInitialBalance())
                                .status(account.getStatus().toString())
                                .build();
                queryUpdateEmitter.emit(e -> true, accountEvent);
        }

        @EventHandler
        public void on(AccountStatusUpdatedEvent event, EventMessage eventMessage) {
                log.info("################# AccountStatusUpdatedEvent ################");
                Account account = accountRepository.findById(event.accountId()).get();
                account.setStatus(event.toStatus());
                accountRepository.save(account);

                AccountEvent accountEvent = AccountEvent.builder()
                                .type(event.getClass().getSimpleName())
                                .accountId(account.getId())
                                .balance(account.getBalance())
                                .status(account.getStatus().toString())
                                .build();
                queryUpdateEmitter.emit(e -> true, accountEvent);
        }

        @EventHandler
        public void on(AccountDebitedEvent event, EventMessage eventMessage) {
                log.info("################# AccountDebitedEvent ################");
                Account account = accountRepository.findById(event.accountId()).get();
                AccountOperation operation = AccountOperation.builder()
                                .date(eventMessage.getTimestamp())
                                .amount(event.amount())
                                .type(OperationType.DEBIT)
                                .account(account)
                                .build();
                AccountOperation savedOperation = operationRepository.save(operation);
                account.setBalance(account.getBalance() - operation.getAmount());
                accountRepository.save(account);
                AccountEvent accountEvent = AccountEvent.builder()
                                .type(event.getClass().getSimpleName())
                                .accountId(account.getId())
                                .balance(account.getBalance())
                                .amount(event.amount())
                                .status(account.getStatus().toString())
                                .build();
                queryUpdateEmitter.emit(e -> true, accountEvent);

        }

        @EventHandler
        public void on(AccountCreditedEvent event, EventMessage eventMessage) {
                log.info("################# AccountCreditedEvent ################");
                Account account = accountRepository.findById(event.getAccountId()).get();
                AccountOperation operation = AccountOperation.builder()
                                .date(eventMessage.getTimestamp())
                                .amount(event.getAmount())
                                .type(OperationType.CREDIT)
                                .account(account)
                                .build();
                AccountOperation savedOperation = operationRepository.save(operation);
                account.setBalance(account.getBalance() + operation.getAmount());
                accountRepository.save(account);
                AccountEvent accountEvent = AccountEvent.builder()
                                .type(event.getClass().getSimpleName())
                                .accountId(account.getId())
                                .balance(account.getBalance())
                                .amount(event.getAmount())
                                .status(account.getStatus().toString())
                                .build();
                queryUpdateEmitter.emit(e -> true, accountEvent);
        }
}
