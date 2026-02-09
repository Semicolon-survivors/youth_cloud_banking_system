package org.example.transactionservice;

import java.util.List;
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
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(java.util.Map.of("error", "Transaction not found: " + id)));
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TransactionRequest request) {
        try {
            restTemplate.getForEntity(
                    accountServiceUrl + "/accounts/" + request.getAccountId(), Object.class);
        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(java.util.Map.of("error", "Account not found: " + request.getAccountId()));
        } catch (HttpClientErrorException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(java.util.Map.of("error", "Account service error: " + ex.getStatusText()));
        }

        Transaction transaction = new Transaction(
                request.getAccountId(),
                request.getAmount(),
                request.getType()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(transaction));
    }
}
