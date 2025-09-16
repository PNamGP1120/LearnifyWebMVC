package com.pnam.repositories;

import com.pnam.pojo.Payment;
import java.util.List;
import java.util.Map;

public interface PaymentRepository {
    List<Payment> getPayments(Map<String, String> params);
    long countPayments(Map<String, String> params);
    Payment findById(Long id);
    void save(Payment p);
    void delete(Long id);
}
