package com.example.cqrs.events;

public record AccountDebitedEvent(String accountId, double amount) {
}

