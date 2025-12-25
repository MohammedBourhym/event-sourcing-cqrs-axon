package com.example.cqrs.commands.commands;


import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class addAccountCommand {
    @TargetAggregateIdentifier
    private String id;
    private Double initBalancel;
    private String currency;
}
