package com.pnam.repositories;

import com.pnam.pojo.Cart;
import java.util.List;

public interface CartRepository {
    Cart findById(Long id);
    List<Cart> findByStudent(Long studentId);
    Cart save(Cart c);
    void delete(Long id);
}
