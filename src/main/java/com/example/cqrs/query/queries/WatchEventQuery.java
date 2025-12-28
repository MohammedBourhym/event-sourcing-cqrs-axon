package com.example.cqrs.query.queries;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public class WatchEventQuery {
    private String accountId;
}
