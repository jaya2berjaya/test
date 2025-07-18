package com.arism.mapper;

import com.arism.dto.CommentDto;
import com.arism.dto.ProductDto;
import com.arism.model.Comment;
import com.arism.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    // Methods to convert Product DTO onject into Product model object and vice-versa
    @Mapping(target = "image", source = "image") //add mapping
    ProductDto toDto(Product product);

    @Mapping(target = "image", source = "image") //add mapping
    Product toEntity(ProductDto productDTO);

    @Mapping(target = "userId",source = "user.id")
    CommentDto toDto(Comment comment);
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "product", ignore = true)
    Comment toEntity(CommentDto commentDTO);
}