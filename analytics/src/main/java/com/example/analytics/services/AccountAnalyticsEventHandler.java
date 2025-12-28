
package com.example.analytics.services;

import org.springframework.stereotype.Service;

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
        AccountAnalytics accountAnalytics = accountAnalyticsRepository.findByAccountId(event.getAccountId());
        accountAnalytics.setBalance(accountAnalytics.getBalance() - event.getAmount());
        accountAnalytics.setTotalDebit(accountAnalytics.getTotalDebit() + event.getAmount());
        accountAnalytics.setTotalNumberOfDebits(accountAnalytics.getTotalNumberOfDebits() + 1);
        accountAnalyticsRepository.save(accountAnalytics);
        queryUpdateEmitter.emit(GetAccountAnalyticsQuery.class,
                (query) -> query.getAccountId().equals(event.getAccountId()),
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