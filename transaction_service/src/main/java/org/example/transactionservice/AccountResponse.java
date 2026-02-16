package org.example.transactionservice;

import java.math.BigDecimal;

public class AccountResponse {

    private Long id;
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
