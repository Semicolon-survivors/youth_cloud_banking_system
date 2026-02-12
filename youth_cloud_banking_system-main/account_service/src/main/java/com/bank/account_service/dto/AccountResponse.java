package com.bank.account_service.dto;

import com.bank.account_service.model.Account;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AccountResponse {
    private Long id;
    private Long userId;
    private String accountNumber;
    private String accountName;
    private String accountHolderName;
    private String accountType;
    private String status;
    private BigDecimal balance;
    private BigDecimal minimumBalance;
    private BigDecimal interestRate;
    private String email;
    private String phoneNumber;
    private LocalDateTime createdAt;

    public static AccountResponse fromEntity(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setUserId(account.getUserId());
        response.setAccountNumber(account.getAccountNumber());
        response.setAccountName(account.getAccountName());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setAccountType(account.getAccountType().name());
        response.setStatus(account.getStatus().name());
        response.setBalance(account.getBalance());
        response.setMinimumBalance(account.getMinimumBalance());
        response.setInterestRate(account.getInterestRate());
        response.setEmail(account.getEmail());
        response.setPhoneNumber(account.getPhoneNumber());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}