package com.example.cqrs.events;

import com.example.cqrs.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreatedEvent {
    private String accountId;
    private Double initBalace;
    private AccountStatus status;
    private String currency;

}
