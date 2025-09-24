package com.ruk.payments.repo;

import com.ruk.payments.entity.EppTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EppTransactionRepository extends JpaRepository<EppTransaction, Long> {
    Optional<EppTransaction> findByOrderKeyAndApplicationUniqueId(String orderKey, String applicationUniqueId);
}
