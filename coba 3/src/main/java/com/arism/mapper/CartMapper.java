package com.arism.mapper;

import com.arism.dto.CartDto;
import com.arism.dto.CartItemDto;
import com.arism.model.Cart;
import com.arism.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    // Methods to convert Cart DTO onject into Cart model object and vice-versa
    @Mapping(target = "userId", source = "user.id")
    CartDto toDto(Cart cart);

    @Mapping(target = "user.id", source = "userId" )
    Cart toEntity(CartDto cartDto);

    @Mapping(target = "productId", source = "product.id")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(target = "product.id", source = "productId")
    CartItem toEntity(CartItemDto cartItemDto);
}
