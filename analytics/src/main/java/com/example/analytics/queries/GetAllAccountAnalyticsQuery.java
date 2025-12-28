package com.example.analytics.queries;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllAccountAnalyticsQuery {
    private String accountId;
}
