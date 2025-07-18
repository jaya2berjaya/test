package com.arism.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// get products without comments
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductListDto {
    private Long id;
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product description is required")
    private String description;
    @Positive(message = "Product price can't be negative")
    private BigDecimal price;
    @PositiveOrZero(message = "Product quantity must be zero or greater")
    private Integer quantity;
    private String image;
}
