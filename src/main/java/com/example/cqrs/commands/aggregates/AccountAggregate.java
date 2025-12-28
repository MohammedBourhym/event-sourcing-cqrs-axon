package com.example.cqrs.commands.aggregates;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import com.example.cqrs.commands.commands.AddAccountCommand;
import com.example.cqrs.commands.commands.CreditAccountCommand;
import com.example.cqrs.enums.AccountStatus;
import com.example.cqrs.events.AccountActivatedEvent;
import com.example.cqrs.events.AccountCreatedEvent;
import com.example.cqrs.events.AccountCreditedEvent;

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
        log.info("########### AddAccountCommand Recieved ##########");
        if (command.getInitBalance() <= 0)
            throw new IllegalArgumentException("Balance must be positive");

        AggregateLifecycle.apply(new AccountCreatedEvent(command.getId(), command.getInitBalance(),
                AccountStatus.CREATED, command.getCurrency()));

        AggregateLifecycle.apply(new AccountActivatedEvent(command.getId(), AccountStatus.ACTIVATED));
    }

    @EventSourcingHandler
    public void on(AccountCreatedEvent event) {
        log.info("########### AccountCreatedEvent ##############");
        this.accountId = event.getAccountId();
        this.balance = event.getInitBalace();
        this.status = event.getStatus();
    }

    @EventSourcingHandler
    public void on(AccountActivatedEvent event) {
        log.info("########### AccountActivatedEvent ##############");
        this.accountId = event.getAccountId();
        this.status = event.getStatus();
    }

    @CommandHandler
    public void handle(CreditAccountCommand command) {
        log.info("########### CreditAccountCommand Recieved ##########");

        if (status.equals(AccountStatus.CREATED))
            throw new RuntimeException("The acc " + command.getId() + " mam2activich");

        if (command.getAmount() <= 0)
            throw new IllegalArgumentException("Amount khso ykon positive");

        AggregateLifecycle.apply(new AccountCreditedEvent(command.getId(), command.getAmount(),
                command.getCurrency()));
    }

    @EventSourcingHandler
    public void on(AccountCreditedEvent event) {
        log.info("########### AccountCreatedEvent ##############");
        this.accountId = event.getAccountId();
        this.balance = this.balance + event.getAmount();
    }
}
