package com.sinch.interview.prepaid.api;

import com.sinch.interview.prepaid.domain.Account;
import com.sinch.interview.prepaid.repository.AccountRepository;
import com.sinch.interview.prepaid.repository.ReservationRepository;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountRepository accountRepository;
    private final ReservationRepository reservationRepository;

    public AccountController(AccountRepository accountRepository, ReservationRepository reservationRepository) {
        this.accountRepository = accountRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping
    public List<AccountResponse> listAccounts() {
        return accountRepository.findAll(Sort.by("id"))
                .stream()
                .map(account -> AccountResponse.from(account, List.of()))
                .toList();
    }

    @GetMapping("/{accountId}")
    public AccountResponse getAccount(@PathVariable String accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(accountId));
        List<ReservationSummary> recentMessages = reservationRepository
                .findTop50ByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(ReservationSummary::from)
                .toList();
        return AccountResponse.from(account, recentMessages);
    }
}
