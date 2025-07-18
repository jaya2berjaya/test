package com.arism.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemDto {
    private Long id;
    private Long productId;
    @Positive(message = "Cannot be negative")
    private Integer quantity;
    @Positive(message = "Cannot be negative")
    private BigDecimal price;
}
