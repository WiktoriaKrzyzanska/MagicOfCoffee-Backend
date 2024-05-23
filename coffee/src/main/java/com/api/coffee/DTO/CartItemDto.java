package com.api.coffee.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    private Long userId;
    private Long productId;
    private int quantity;
}
