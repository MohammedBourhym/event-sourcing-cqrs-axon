package com.example.analytics.repositories;

import com.example.analytics.entities.AccountAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAnalyticsRepository extends JpaRepository<AccountAnalytics, String> {

    AccountAnalytics findByAccountId(String accountId);
}
