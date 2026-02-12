package org.example.transactionservice;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository repository;
    private final RestTemplate restTemplate;

    @Value("${account-service.url}")
    private String accountServiceUrl;

    public TransactionController(TransactionRepository repository, RestTemplate restTemplate) {
        this.repository = repository;
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public List<Transaction> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        Optional<Transaction> txOpt = repository.findById(id);
        if (txOpt.isPresent()) {
            return ResponseEntity.ok(txOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(java.util.Map.of("error", "Transaction not found: " + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionRequest request) {

        try {
            // 1️⃣ Get Account from Account Service
            ResponseEntity<AccountResponse> response =
                    restTemplate.getForEntity(
                            accountServiceUrl + "/api/accounts/" + request.getAccountId(),
                            AccountResponse.class
                    );

            AccountResponse account = response.getBody();

            if (account == null) {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of("error", "Account not found"));
            }

            BigDecimal currentBalance = account.getBalance();
            BigDecimal amount = request.getAmount();

            BigDecimal balanceChange;

            // 2️⃣ Business Logic
            if ("DEPOSIT".equalsIgnoreCase(request.getType())) {
                balanceChange = amount;
            } else if ("WITHDRAW".equalsIgnoreCase(request.getType())) {

                if (currentBalance.compareTo(amount) < 0) {
                    return ResponseEntity.badRequest()
                            .body(java.util.Map.of("error", "Insufficient balance"));
                }

                balanceChange = amount.negate();
            } else {
                return ResponseEntity.badRequest()
                        .body(java.util.Map.of("error", "Invalid transaction type"));
            }

            // 3️⃣ Update Account Balance
            restTemplate.put(
                    accountServiceUrl + "/api/accounts/" + request.getAccountId() +
                            "/balance?amount=" + balanceChange,
                    null
            );

            // 4️⃣ Save Transaction
            Transaction transaction = new Transaction(
                    request.getAccountId(),
                    amount,
                    request.getType()
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(repository.save(transaction));

        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", "Account service error"));
        }
    }

}
