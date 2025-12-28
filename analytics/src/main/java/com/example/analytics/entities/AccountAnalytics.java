package com.example.analytics.entities;

import lombok.Data;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountAnalytics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String accountId;
    private double balance;
    private double totalDebit;
    private double totalCredit;
    private Integer totalNumberOfDebits;
    private Integer totalNumberOfCredits;

}
