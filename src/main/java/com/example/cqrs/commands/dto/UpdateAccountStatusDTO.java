package com.example.cqrs.commands.dto;

import com.example.cqrs.enums.AccountStatus;

public record UpdateAccountStatusDTO(String accountId, AccountStatus accountStatus) {
}
