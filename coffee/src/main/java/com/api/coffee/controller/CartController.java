package com.api.coffee.controller;

import com.api.coffee.DTO.CartItemDto;
import com.api.coffee.DTO.SubscriptionDto;
import com.api.coffee.entity.Cart;
import com.api.coffee.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/coffee/cart")
public class CartController {
    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addToCart(@RequestBody CartItemDto cartItemDto) {
        System.out.println("Działa");
        cartService.addToCart(Math.toIntExact(cartItemDto.getUserId()), cartItemDto.getProductId(), cartItemDto.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/remove")
    public ResponseEntity<Void> removeFromCart(@RequestBody CartItemDto cartItemDto) {
        cartService.removeFromCart(cartItemDto.getUserId(), cartItemDto.getProductId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateQuantity(@RequestBody CartItemDto cartItemDto) {
        cartService.updateQuantity(cartItemDto.getUserId(), cartItemDto.getProductId(), cartItemDto.getQuantity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/subscription")
    public ResponseEntity<Void> addSubscription(@RequestBody SubscriptionDto subscriptionDto) {
        cartService.addSubscription(subscriptionDto.getUserId(), subscriptionDto.getSubscriptionId());
        return ResponseEntity.ok().build();
    }
}
