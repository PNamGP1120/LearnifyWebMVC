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
@RequestMapping("/api/cart")
@CrossOrigin
public class ApiCartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartItemService cartItemService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    // ===== ADD TO CART =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @PostMapping
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> payload, Principal principal) {
        User student = userService.getUserByUsername(principal.getName());

        if (payload.get("courseId") == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Khoá học bắt buộc"));
        }
        Long courseId = Long.parseLong(payload.get("courseId").toString());
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Khoá học không tồn tại"));
        }

        // lấy hoặc tạo cart
        List<Cart> carts = cartService.getCartsByStudent(student.getId());
        Cart cart;
        if (carts.isEmpty()) {
            cart = new Cart();
            cart.setStudentId(student);
            cart.setCreatedAt(new Date());
            cart.setStatus("ACTIVE");
            cart = cartService.createCart(cart);
        } else {
            cart = carts.get(0);
        }

        // kiểm tra trùng
        if (cartItemService.getCartItemByCartAndCourse(cart.getId(), courseId) != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Khoá học đã có trong giỏ"));
        }

        CartItem item = new CartItem();
        item.setCartId(cart);
        item.setCourseId(course);
        item.setAddedAt(new Date());

        cartItemService.createCartItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }

    // ===== VIEW CART =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @GetMapping
    public ResponseEntity<List<CartItem>> myCart(Principal principal) {
        User student = userService.getUserByUsername(principal.getName());
        List<Cart> carts = cartService.getCartsByStudent(student.getId());
        if (carts.isEmpty()) return ResponseEntity.ok(List.of());

        return ResponseEntity.ok(cartItemService.getCartItemsByCart(carts.get(0).getId()));
    }

    // ===== REMOVE ITEM =====
    @PreAuthorize("hasAuthority('STUDENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeFromCart(@PathVariable("id") Long id, Principal principal) {
        CartItem item = cartItemService.getCartItemById(id);
        if (item == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Item không tồn tại"));
        }

        User student = userService.getUserByUsername(principal.getName());
        if (!item.getCartId().getStudentId().getId().equals(student.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", "Không có quyền xoá item này"));
        }

        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
