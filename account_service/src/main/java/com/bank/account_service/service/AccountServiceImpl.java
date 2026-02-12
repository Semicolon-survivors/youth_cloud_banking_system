package com.bank.account_service.service;

import com.bank.account_service.config.YouthBankProperties;
import com.bank.account_service.model.Account;
import com.bank.account_service.repository.AccountRepository;
import com.bank.account_service.util.IdNumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final YouthBankProperties youthBankProperties;

    @Override
    @Transactional
    public Account createAccount(Account account) {

        // 1️⃣ Prevent duplicate ID numbers
        if (accountRepository.existsByIdNumber(account.getIdNumber())) {
            throw new IllegalArgumentException("An account already exists for this ID Number");
        }

        // 2️⃣ Age Qualification Check
        int age = IdNumberUtil.calculateAge(account.getIdNumber());

        if (age < youthBankProperties.getMinAge() || age > youthBankProperties.getMaxAge()) {
            throw new IllegalArgumentException(
                    "User does not qualify for Youth Bank account. Age: " + age +
                            " (Allowed: " + youthBankProperties.getMinAge() + "-" + youthBankProperties.getMaxAge() + ")"
            );
        }

        // 3️⃣ Initial Deposit Validation
        if (account.getBalance().compareTo(youthBankProperties.getMaxInitialDeposit()) > 0) {
            throw new IllegalArgumentException(
                    "Initial deposit exceeds Youth Bank limit: " + youthBankProperties.getMaxInitialDeposit()
            );
        }

        // 4️⃣ Assign Interest Rate based on Account Type
        String key = account.getAccountType().name().toLowerCase().replace("_", "-");

        BigDecimal rate = youthBankProperties.getInterestRates().get(key);

        if (rate == null) {
            throw new IllegalArgumentException("Interest rate not configured for account type: " + key);
        }

        account.setInterestRate(rate);

        // 5️⃣ Generate Account Number
        String accountNumber = generateAccountNumber(account.getAccountType());
        account.setAccountNumber(accountNumber);

        log.info("Creating Youth Bank account: {} for user {} age {}",
                accountNumber, account.getUserId(), age);

        return accountRepository.save(account);
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

    @Override
    @Transactional
    public Account updateBalance(Long accountId, BigDecimal amount) {

        Account account = getAccountById(accountId);

        BigDecimal newBalance = account.getBalance().add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        account.setBalance(newBalance);

        log.info("Updated balance for account {}. New balance: {}", accountId, newBalance);

        return accountRepository.save(account);
    }

    private String generateAccountNumber(Account.AccountType type) {

        String prefix = switch (type) {
            case STUDENT_SAVINGS -> "SS";
            case TEEN_CURRENT -> "TC";
            case EDUCATION_SAVINGS -> "ES";
            case YOUTH_INVESTMENT -> "YI";
        };

        return prefix + String.format("%08d", new Random().nextInt(100000000));
    }
}
