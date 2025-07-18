package com.arism.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    @NotNull(message = "Content is required")
    private String content;
    @Min(value = 1)
    @Max(value = 5)
    private Integer score;
    private Long userId;
}
