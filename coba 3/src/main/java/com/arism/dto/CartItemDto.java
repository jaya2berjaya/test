package com.arism.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    @Positive(message = "Cannot be negative")
    private Integer quantity;
}
