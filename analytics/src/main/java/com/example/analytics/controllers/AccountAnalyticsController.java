package com.example.analytics.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@Slf4j
public class AccountAnalyticsController {

    private QueryGateway queryGateway;

    @GetMapping("/query/accountAnalytics")
    public List<AccountAnalytics> getAccountAnalytics() {
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