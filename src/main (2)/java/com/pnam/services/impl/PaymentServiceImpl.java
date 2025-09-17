package com.pnam.services.impl;

import com.pnam.pojo.Payment;
import com.pnam.repositories.PaymentRepository;
import com.pnam.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository repo;

    @Override
    public List<Payment> getPayments(Map<String, String> params) {
        return repo.getPayments(params);
    }

    @Override
    public long countPayments(Map<String, String> params) {
        return repo.countPayments(params);
    }

    @Override
    public Payment getPaymentById(Long id) {
        return repo.findById(id);
    }

    @Override
    public void savePayment(Payment p) {
        repo.save(p);
    }

    @Override
    public void deletePayment(Long id) {
        repo.delete(id);
    }
}
