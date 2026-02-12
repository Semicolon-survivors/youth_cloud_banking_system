package com.bank.account_service.dto;

import com.bank.account_service.model.Account;
import com.bank.account_service.util.IdNumberUtil;

import java.math.BigDecimal;

public class CreateAccountRequest {

    private Long userId;
    private String idNumber;
    private String accountName;
    private String accountHolderName;
    private String accountType;
    private BigDecimal initialDeposit = BigDecimal.ZERO;
    private String email;
    private String phoneNumber;
    private String address;

    // Manual validation
    public void validate() {
        if (userId == null) {
            throw new IllegalArgumentException("User ID is required");
        }
        if (idNumber == null || idNumber.isBlank()) {
            throw new IllegalArgumentException("ID Number is required");
        }
        if (idNumber.length() != 13) {
            throw new IllegalArgumentException("ID Number must be 13 digits");
        }
        if (accountName == null || accountName.isBlank()) {
            throw new IllegalArgumentException("Account name is required");
        }
        if (accountHolderName == null || accountHolderName.isBlank()) {
            throw new IllegalArgumentException("Account holder name is required");
        }
        if (accountType == null || accountType.isBlank()) {
            throw new IllegalArgumentException("Account type is required");
        }
    }

    // Convert DTO to Account entity
    public Account toEntity() {

        Account account = new Account();
        account.setUserId(this.userId);
        account.setIdNumber(this.idNumber);

        // Extract DOB from ID Number
        account.setDateOfBirth(IdNumberUtil.extractDateOfBirth(this.idNumber));

        account.setAccountName(this.accountName);
        account.setAccountHolderName(this.accountHolderName);
        account.setAccountType(Account.AccountType.valueOf(this.accountType.toUpperCase()));
        account.setBalance(this.initialDeposit);
        account.setEmail(this.email);
        account.setPhoneNumber(this.phoneNumber);
        account.setAddress(this.address);

        return account;
    }

    // Getters and Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getIdNumber() { return idNumber; }
    public void setIdNumber(String idNumber) { this.idNumber = idNumber; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getInitialDeposit() { return initialDeposit; }
    public void setInitialDeposit(BigDecimal initialDeposit) { this.initialDeposit = initialDeposit; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}
