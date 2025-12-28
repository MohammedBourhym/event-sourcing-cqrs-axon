package com.example.cqrs.commands.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cqrs.commands.commands.AddAccountCommand;
import com.example.cqrs.commands.commands.CreditAccountCommand;
import com.example.cqrs.commands.commands.DebitAccountCommand;
import com.example.cqrs.commands.dto.AddNewAccountReqDTO;
import com.example.cqrs.commands.dto.CreditAccountRequestDTO;
import com.example.cqrs.commands.dto.DebitAccountDTO;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/commands")
public class AccountCommandController {

    private final CommandGateway commandGateway;
    private final EventStore eventStore;

    AccountCommandController(CommandGateway commandGateway, EventStore eventStore) {
        this.commandGateway = commandGateway;
        this.eventStore = eventStore;
    }

    @PostMapping("/add")
    public CompletableFuture<String> addNewAccount(@RequestBody AddNewAccountReqDTO request) {
        CompletableFuture<String> response = commandGateway.send(new AddAccountCommand(
                UUID.randomUUID().toString(),
                request.initBalance(),
                request.currency()));
        return response;
    }

    @PostMapping("/credit")
    public CompletableFuture<String> creditAccount(@RequestBody CreditAccountRequestDTO request) {
        CompletableFuture<String> response = commandGateway
                .send(new CreditAccountCommand(request.accountId(), request.amount(), request.currency()));
        return response;
    }

    @GetMapping("/events/{accountId}")
    public Stream eventStream(@PathVariable String accountId) {
        return eventStore.readEvents(accountId).asStream();
    }

    @PostMapping("/debit")
    public CompletableFuture<String> debitAccount(@RequestBody DebitAccountDTO request) {
        CompletableFuture<String> result = this.commandGateway.send(new DebitAccountCommand(
                request.accountId(),
                request.amount()));
        return result;
    }

   
    @ExceptionHandler(Exception.class)
    public String exceptionHandler(Exception exception) {
        return exception.getMessage();
    }

}
