package com.pnam.services;

import com.pnam.pojo.Cart;
import java.util.List;

public interface CartService {
    Cart getCartById(Long id);
    List<Cart> getCartsByStudent(Long studentId);
    Cart createCart(Cart c);
    Cart updateCart(Cart c);
    void deleteCart(Long id);
}
