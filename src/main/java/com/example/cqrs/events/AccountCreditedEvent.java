package com.example.cqrs.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountCreditedEvent {
    private String accountId;
    Double amount;
    String currency;

}
