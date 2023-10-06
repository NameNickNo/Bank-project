package com.project.account.repository;

import com.project.account.model.BalanceOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceOperationRepository extends JpaRepository<BalanceOperation, Long> {
}
