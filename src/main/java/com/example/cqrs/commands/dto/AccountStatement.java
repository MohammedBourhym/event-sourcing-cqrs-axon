package com.example.cqrs.commands.dto;

import java.util.List;

import com.example.cqrs.query.entities.Account;
import com.example.cqrs.query.entities.AccountOperation;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor @Getter
public class AccountStatement {
    private Account account;
    private List<AccountOperation> operations;
}
