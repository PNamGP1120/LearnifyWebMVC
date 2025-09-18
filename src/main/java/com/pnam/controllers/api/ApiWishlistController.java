//package com.pnam.controllers.api;
//
//import com.pnam.pojo.Wishlist;
//import com.pnam.services.WishlistService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/wishlist")
//@CrossOrigin
//public class ApiWishlistController {
//
//    @Autowired
//    private WishlistService wishlistService;
//
//    @GetMapping
//    public ResponseEntity<List<Wishlist>> list(@RequestParam Map<String, String> params) {
//        return ResponseEntity.ok(wishlistService.getWishlists(params));
//    }
//
//    @PostMapping
//    public ResponseEntity<Wishlist> create(@RequestBody Wishlist w) {
//        wishlistService.saveWishlist(w);
//        return ResponseEntity.ok(w);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        wishlistService.deleteWishlist(id);
//        return ResponseEntity.noContent().build();
//    }
//}
