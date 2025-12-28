package com.example.cqrs.events;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.example.cqrs.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    @JsonAlias("initBalace")
    private Double initialBalance;
    private AccountStatus status;
    private String currency;

}
