package com.example.analytics.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAnalytics {
    @Id
    private String accountId;
    private double balance;
    private double totalDebit;
    private double totalCredit;
    private Integer totalNumberOfDebits;
    private Integer totalNumberOfCredits;

}
