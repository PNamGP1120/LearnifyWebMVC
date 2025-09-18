package com.pnam.repositories;

import com.pnam.pojo.CartItem;
import java.util.List;

public interface CartItemRepository {

    CartItem findById(Long id);

    List<CartItem> findByCart(Long cartId);

    CartItem findByCartAndCourse(Long cartId, Long courseId);

    CartItem save(CartItem ci);

    void delete(Long id);
}
