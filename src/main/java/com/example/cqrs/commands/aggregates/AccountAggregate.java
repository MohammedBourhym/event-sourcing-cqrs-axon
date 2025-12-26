package com.example.cqrs.commands.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.cqrs.commands.commands.AddAccountCommand;
import com.example.cqrs.enums.AccountStatus;
import com.example.cqrs.events.AccountCreatedEvent;

import lombok.extern.slf4j.Slf4j;

@Aggregate
@Slf4j
public class AccountAggregate {
    @AggregateIdentifier
    private String accountId;
    private Double balance;
    private AccountStatus status;

    public AccountAggregate() {
    }

    @CommandHandler
    public AccountAggregate(AddAccountCommand command) {
        if (command.getInitBalance() <= 0)
            throw new IllegalArgumentException("Balance must be positive");

        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getInitBalance(),
                AccountStatus.CREATED, command.getCurrency()));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        this.accountId = event.getAccountId();
        this.balance = event.getInitBalace();
        this.status = event.getStatus();
    }
}
