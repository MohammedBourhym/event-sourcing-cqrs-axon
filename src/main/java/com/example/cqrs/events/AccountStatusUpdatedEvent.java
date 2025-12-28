package com.example.cqrs.events;

import com.example.cqrs.enums.AccountStatus;

public record AccountStatusUpdatedEvent(String accountId, AccountStatus fromStatus, AccountStatus toStatus) {
}

