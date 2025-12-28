package com.example.cqrs.query.controllers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.commands.dto.AccountStatement;
import com.example.cqrs.commands.dto.AccountEvent;
import com.example.cqrs.query.entities.Account;
import com.example.cqrs.query.queries.GetAccountStatementQuery;
import com.example.cqrs.query.queries.GetAllAccountsQuery;
import com.example.cqrs.query.queries.WatchEventQuery;

import reactor.core.publisher.Flux;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/query/accounts")
@CrossOrigin("*")
public class AccountQueryController {
    private QueryGateway queryGateway;

    public AccountQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/all")
    public CompletableFuture<List<Account>> getAllAccounts() {
        CompletableFuture<List<Account>> result = queryGateway.query(new GetAllAccountsQuery(),
                ResponseTypes.multipleInstancesOf(Account.class));
        return result;
    }

    @GetMapping("/statement/{accountId}")
    public CompletableFuture<AccountStatement> getAccountStatement(@PathVariable String accountId) {
        CompletableFuture<AccountStatement> result = queryGateway.query(new GetAccountStatementQuery(accountId),
                ResponseTypes.instanceOf(AccountStatement.class));
        return result;
    }



    @GetMapping(value = "/watch/{accountId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountEvent> watch(@PathVariable String accountId){
        SubscriptionQueryResult<AccountEvent, AccountEvent> result = queryGateway.subscriptionQuery(new WatchEventQuery(accountId),
                ResponseTypes.instanceOf(AccountEvent.class),
                ResponseTypes.instanceOf(AccountEvent.class)
        );
        return result.initialResult().concatWith(result.updates());
    }

    
}
