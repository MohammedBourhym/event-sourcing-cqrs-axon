package com.example.cqrs.commands.dto;

public record CreditAccountRequestDTO(String accountId,Double amount,String currency) {
}