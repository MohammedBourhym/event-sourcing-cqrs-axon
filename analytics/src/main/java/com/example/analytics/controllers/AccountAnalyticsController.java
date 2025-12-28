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
    public AccountAnalytics getAccountAnalytics(@PathVariable String accountId) {
        return queryGateway.query(new GetAccountAnalyticsQuery(accountId),
                ResponseTypes.instanceOf(AccountAnalytics.class));
    }

    @QueryHandler
    public List<AccountAnalytics> on(GetAllAccountAnalyticsQuery query) {
        return accountAnalyticsRepository.findAll();
    }

    @QueryHandler
    public AccountAnalytics on(GetAccountAnalyticsQuery query) {
        return accountAnalyticsRepository.findByAccountId(query.getAccountId());
    }
}
