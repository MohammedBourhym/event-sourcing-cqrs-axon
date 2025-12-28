
package com.example.cqrs.query.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cqrs.query.entities.Account;

public interface AccountRepository extends JpaRepository<Account, String> {
}
