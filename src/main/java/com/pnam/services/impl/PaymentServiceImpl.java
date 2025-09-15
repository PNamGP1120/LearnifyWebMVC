package com.pnam.services.impl;

import com.pnam.pojo.Payment;
import com.pnam.repositories.PaymentRepository;
import com.pnam.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepo;

    @Override
    public Payment getPaymentById(Long id) {
        return paymentRepo.findById(id);
    }

    @Override
    public List<Payment> getPaymentsByEnrollment(Long enrollmentId) {
        return paymentRepo.findByEnrollment(enrollmentId);
    }

    @Override
    public Payment createPayment(Payment p) {
        return paymentRepo.save(p);
    }

    @Override
    public Payment updatePayment(Payment p) {
        return paymentRepo.save(p);
    }

    @Override
    public void deletePayment(Long id) {
        paymentRepo.delete(id);
    }
    
    @Override
    public List<Payment> getAllPayments() {
        return paymentRepo.findAll();
    }
}
