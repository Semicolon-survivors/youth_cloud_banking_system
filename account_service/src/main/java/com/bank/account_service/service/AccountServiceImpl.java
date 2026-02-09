package com.bank.account_service.service;

import com.bank.account_service.model.Account;
import com.bank.account_service.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Override
    @Transactional
    public Account createAccount(Account account) {
        validateUserExists(account.getUserId());
        // Generate account number
        String accountNumber = generateAccountNumber(account.getAccountType());
        account.setAccountNumber(accountNumber);

        log.info("Creating account: {}", accountNumber);
        return accountRepository.save(account);
    }

    private void validateUserExists(Long userId) {
        try {
            restTemplate.getForEntity(userServiceUrl + "/users/" + userId, Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            throw new IllegalArgumentException("User not found: " + userId);
        } catch (HttpClientErrorException ex) {
            throw new IllegalArgumentException("User service error: " + ex.getStatusCode());
        }
    }

    @Override
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found: " + id));
    }

    @Override
    public List<Account> getAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public Account updateAccountStatus(Long accountId, Account.AccountStatus status) {
        Account account = getAccountById(accountId);
        account.setStatus(status);
        if (status == Account.AccountStatus.CLOSED) {
            account.setClosedAt(java.time.LocalDateTime.now());
        }
        log.info("Updated account {} status to {}", accountId, status);
        return accountRepository.save(account);
    }

    private String generateAccountNumber(Account.AccountType type) {
        String prefix = switch (type) {
            case STUDENT_SAVINGS -> "SS";
            case TEEN_CURRENT -> "TC";
            case EDUCATION_SAVINGS -> "ES";
            case YOUTH_INVESTMENT -> "YI";
            default -> "AC";
        };
        return prefix + String.format("%08d", new java.util.Random().nextInt(100000000));
    }
}
