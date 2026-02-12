package com.bank.account_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "youthbank")
@Data
public class YouthBankProperties {

    private int minAge;
    private int maxAge;
    private BigDecimal maxInitialDeposit;
    private BigDecimal maxDailyWithdrawal;

    private Map<String, BigDecimal> interestRates;
}
