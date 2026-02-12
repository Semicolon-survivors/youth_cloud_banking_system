package com.bank.account_service.service;

import com.bank.account_service.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountById(Long id);
    List<Account> getAccountsByUserId(Long userId);
    Account updateAccountStatus(Long accountId, Account.AccountStatus status);
    Account updateBalance(Long accountId, BigDecimal amount);

}
