
package com.example.analytics.services;

import com.example.analytics.entities.AccountAnalytics;
import com.example.analytics.queries.GetAccountAnalyticsQuery;
import com.example.analytics.queries.GetAllAccountAnalyticsQuery;
import com.example.analytics.repositories.AccountAnalyticsRepository;
import com.example.cqrs.events.AccountCreatedEvent;
import com.example.cqrs.events.AccountCreditedEvent;
import com.example.cqrs.events.AccountDebitedEvent;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AccountAnalyticsEventHandler {
    private AccountAnalyticsRepository accountAnalyticsRepository;

    private QueryUpdateEmitter queryUpdateEmitter;

    @EventHandler
    public void on(AccountCreatedEvent event) {
        log.info("AccountCreatedEvent {}", event);
        AccountAnalytics accountAnalytics = AccountAnalytics.builder()
                .accountId(event.getAccountId())
                .balance(event.getInitialBalance())
                .totalDebit(0.0)
                .totalCredit(0.0)
                .totalNumberOfDebits(0)
                .totalNumberOfCredits(0)
                .build();
        accountAnalyticsRepository.save(accountAnalytics);
    }

    @EventHandler
    public void on(AccountDebitedEvent event) {
        log.info("AccountDebitedEvent {}", event);
        AccountAnalytics accountAnalytics = accountAnalyticsRepository.findByAccountId(event.accountId());
        accountAnalytics.setBalance(accountAnalytics.getBalance() - event.amount());
        accountAnalytics.setTotalDebit(accountAnalytics.getTotalDebit() + event.amount());
        accountAnalytics.setTotalNumberOfDebits(accountAnalytics.getTotalNumberOfDebits() + 1);
        accountAnalyticsRepository.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsQuery.class,
                (query) -> query.getAccountId().equals(event.accountId()),
                accountAnalytics);
    }

    @EventHandler
    public void on(AccountCreditedEvent event) {
        log.info("AccountCreditedEvent {}", event);
        AccountAnalytics accountAnalytics = accountAnalyticsRepository.findByAccountId(event.getAccountId());
        accountAnalytics.setBalance(accountAnalytics.getBalance() + event.getAmount());
        accountAnalytics.setTotalCredit(accountAnalytics.getTotalCredit() + event.getAmount());
        accountAnalytics.setTotalNumberOfCredits(accountAnalytics.getTotalNumberOfCredits() + 1);
        accountAnalyticsRepository.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsQuery.class,
                (query) -> query.getAccountId().equals(event.getAccountId()),
                accountAnalytics);
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