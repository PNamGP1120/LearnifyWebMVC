package com.pnam.services;

import com.pnam.pojo.Payment;
import java.util.List;
import java.util.Map;

public interface PaymentService {

    List<Payment> getPayments(Map<String, String> params);

    long countPayments(Map<String, String> params);

    Payment getPaymentById(Long id);

    void savePayment(Payment p);

    void deletePayment(Long id);
}
