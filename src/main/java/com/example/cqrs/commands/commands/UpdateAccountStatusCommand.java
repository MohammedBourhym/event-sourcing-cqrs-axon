package com.example.cqrs.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import com.example.cqrs.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class UpdateAccountStatusCommand {
    @TargetAggregateIdentifier
    private String id;
    private AccountStatus accountStatus;
}
