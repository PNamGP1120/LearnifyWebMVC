package com.pnam.controllers.api;

import com.pnam.pojo.Cart;
import com.pnam.pojo.CartItem;
import com.pnam.pojo.Course;
import com.pnam.pojo.User;
import com.pnam.services.CartItemService;
import com.pnam.services.CartService;
import com.pnam.services.CourseService;
import com.pnam.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart/items")
@CrossOrigin
public class ApiCartItemController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private UserService userService;

    @Autowired
    private CourseService courseService;

    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping
    public ResponseEntity<?> addItem(@RequestBody Map<String, Long> body, Principal principal) {
        Long courseId = body.get("courseId");
        if (courseId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "courseId là bắt buộc"));
        }

        User student = userService.getUserByUsername(principal.getName());

        List<Cart> carts = cartService.getCartsByStudent(student.getId());
        Cart cart = carts.stream()
                .filter(c -> "ACTIVE".equals(c.getStatus()))
                .findFirst()
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setStudentId(student);
                    newCart.setCreatedAt(new Date());
                    newCart.setStatus("ACTIVE");
                    return cartService.createCart(newCart);
                });

        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Khoá học không tồn tại"));
        }

        CartItem existed = cartItemService.getCartItemByCartAndCourse(cart.getId(), courseId);
        if (existed != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "Khoá học đã có trong giỏ hàng"));
        }

        CartItem item = new CartItem();
        item.setCartId(cart);
        item.setCourseId(course);
        item.setAddedAt(new Date());

        CartItem saved = cartItemService.createCartItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeItem(@PathVariable("id") Long id, Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        CartItem item = cartItemService.getCartItemById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "CartItem không tồn tại"));
        }

        if (!item.getCartId().getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xoá item này"));
        }

        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
