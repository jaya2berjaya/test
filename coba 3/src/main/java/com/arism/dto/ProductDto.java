package com.arism.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductDto {
    private Long id;
    // Validation
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Product description is required")
    private String description;
    @Positive(message = "Cannot be negative")
    private BigDecimal price;
    @Positive(message = "Cannot be negative")
    private Integer quantity;
    private String image;

    private List<CommentDto> comments;
}
