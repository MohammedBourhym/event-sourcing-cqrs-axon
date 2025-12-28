package com.example.cqrs.commands.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.AllArgsConstructor;
import lombok.Getter;



@Getter @AllArgsConstructor
public class DebitAccountCommand {
 @TargetAggregateIdentifier
    private String id;
    private double amount;
}