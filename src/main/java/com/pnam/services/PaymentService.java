package com.pnam.services;

import com.pnam.pojo.Payment;
import java.util.List;

public interface PaymentService {
    Payment getPaymentById(Long id);
    List<Payment> getPaymentsByEnrollment(Long enrollmentId);
    Payment createPayment(Payment p);
    Payment updatePayment(Payment p);
    void deletePayment(Long id);
    List<Payment> getAllPayments();
}
