package com.pnam.services.impl;

import com.pnam.pojo.Cart;
import com.pnam.repositories.CartRepository;
import com.pnam.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepo;

    @Override
    public Cart getCartById(Long id) {
        return cartRepo.findById(id);
    }

    @Override
    public List<Cart> getCartsByStudent(Long studentId) {
        return cartRepo.findByStudent(studentId);
    }

    @Override
    public Cart createCart(Cart c) {
        return cartRepo.save(c);
    }

    @Override
    public Cart updateCart(Cart c) {
        return cartRepo.save(c);
    }

    @Override
    public void deleteCart(Long id) {
        cartRepo.delete(id);
    }
}
