package me.aurum.springsecurity01securityform.Account;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {

    Account findByUsername(String username);
}
