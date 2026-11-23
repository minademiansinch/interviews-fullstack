package com.sinch.interview.prepaid.repository;

import com.sinch.interview.prepaid.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, String> {
}
