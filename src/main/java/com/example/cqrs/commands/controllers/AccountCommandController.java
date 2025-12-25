package com.example.cqrs.commands.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.commands.commands.addAccountCommand;
import com.example.cqrs.commands.dto.AddNewAccountReqDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/commands")
public class AccountCommandController {

    private CommandGateway commandGateway;

    AccountCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountReqDTO request) {
        CompletableFuture<String> response = commandGateway.send(new addAccountCommand(
                UUID.randomUUID().toString(),
                request.initBalance(),
                request.currency()));
        return response;
    }

}
