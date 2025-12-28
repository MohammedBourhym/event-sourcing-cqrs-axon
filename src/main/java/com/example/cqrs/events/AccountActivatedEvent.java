package com.example.cqrs.events;

import com.example.cqrs.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountActivatedEvent {
    private String accountId;
    private AccountStatus status;

}
