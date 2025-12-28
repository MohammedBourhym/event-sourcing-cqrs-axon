package com.example.analytics.controllers;

import com.example.analytics.entities.AccountAnalytics;
import com.example.analytics.queries.GetAccountAnalyticsQuery;
import com.example.analytics.queries.GetAllAccountAnalyticsQuery;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@Slf4j
public class AccountAnalyticsController {

    private QueryGateway queryGateway;

    @GetMapping("/query/accountAnalytics")
    public CompletableFuture<List<AccountAnalytics>> getAccountAnalytics() {
        return queryGateway.query(new GetAllAccountAnalyticsQuery(),
                ResponseTypes.multipleInstancesOf(AccountAnalytics.class));
    }

    @GetMapping("/query/accountAnalytics/{accountId}")
    public CompletableFuture<AccountAnalytics> getAccountAnalytics(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountAnalyticsQuery(accountId),
                ResponseTypes.instanceOf(AccountAnalytics.class));
    }

    @GetMapping(value = "/query/accountAnalytics/{accountId}/watch", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<AccountAnalytics> getAccountAnalyticsWatch(@PathVariable String accountId) {

        SubscriptionQueryResult<AccountAnalytics, AccountAnalytics> subscriptionQueryResult = queryGateway
                .subscriptionQuery(new GetAccountAnalyticsQuery(accountId), AccountAnalytics.class,
                        AccountAnalytics.class);

        return subscriptionQueryResult.initialResult().concatWith(subscriptionQueryResult.updates());
    }

}