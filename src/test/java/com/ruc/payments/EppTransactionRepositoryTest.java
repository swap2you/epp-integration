package com.ruc.payments;

import com.ruc.payments.entity.EppTransaction;
import com.ruc.payments.repo.EppTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class EppTransactionRepositoryTest {
    @Autowired
    private EppTransactionRepository repo;

    @Test
    void saveAndFindByOrderKeyAndApplicationUniqueId() {
        EppTransaction tx = new EppTransaction();
        tx.setOrderKey("ORD1");
        tx.setApplicationUniqueId("APP1");
        tx.setStatus("APP");
        tx.setAmount(new BigDecimal("1.00"));
        tx.setEmail("a@b.com");
        tx.setRawRequest("{}");
        tx.setRawResponse("{}");

        repo.save(tx);

        Optional<EppTransaction> found = repo.findByOrderKeyAndApplicationUniqueId("ORD1", "APP1");
        assertTrue(found.isPresent());
        assertEquals("APP", found.get().getStatus());
    }
}
