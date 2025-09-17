package com.pnam.services;

import com.pnam.pojo.CartItem;
import java.util.List;

public interface CartItemService {
    CartItem getCartItemById(Long id);
    List<CartItem> getCartItemsByCart(Long cartId);
    CartItem getCartItemByCartAndCourse(Long cartId, Long courseId);
    CartItem createCartItem(CartItem ci);
    CartItem updateCartItem(CartItem ci);
    void deleteCartItem(Long id);
}
