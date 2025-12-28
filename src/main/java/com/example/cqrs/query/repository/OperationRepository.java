package com.example.cqrs.query.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cqrs.query.entities.AccountOperation;

public interface OperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountId(String id);
}
