package com.pnam.services.impl;

import com.pnam.pojo.CartItem;
import com.pnam.repositories.CartItemRepository;
import com.pnam.services.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private CartItemRepository cartItemRepo;

    @Override
    public CartItem getCartItemById(Long id) {
        return cartItemRepo.findById(id);
    }

    @Override
    public List<CartItem> getCartItemsByCart(Long cartId) {
        return cartItemRepo.findByCart(cartId);
    }

    @Override
    public CartItem getCartItemByCartAndCourse(Long cartId, Long courseId) {
        return cartItemRepo.findByCartAndCourse(cartId, courseId);
    }

    @Override
    public CartItem createCartItem(CartItem ci) {
        return cartItemRepo.save(ci);
    }

    @Override
    public CartItem updateCartItem(CartItem ci) {
        return cartItemRepo.save(ci);
    }

    @Override
    public void deleteCartItem(Long id) {
        cartItemRepo.delete(id);
    }
}
