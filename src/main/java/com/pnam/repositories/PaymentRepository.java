package com.pnam.repositories;

import com.pnam.pojo.Payment;
import java.util.List;

public interface PaymentRepository {
    Payment findById(Long id);
    List<Payment> findByEnrollment(Long enrollmentId);
    Payment save(Payment p);
    void delete(Long id);
    List<Payment> findAll();
}
