package com.project.account.repository;

import com.project.account.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferOperationRepository extends JpaRepository<Transfer, Long> {
}
