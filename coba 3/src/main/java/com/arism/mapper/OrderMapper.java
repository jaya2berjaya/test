package com.arism.mapper;

import com.arism.dto.CartItemDto;
import com.arism.dto.OrderDto;
import com.arism.dto.OrderItemDto;
import com.arism.model.Cart;
import com.arism.model.Order;
import com.arism.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    // Methods to convert Order DTO onject into Order model object and vice-versa
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderItems", source = "items") // based on the properties set on model/dto
    OrderDto toDto(Order order);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "items", source = "orderItems")
    Order toEntity(OrderDto orderDto);

    List<OrderDto> toDtos(List<Order> orders);
    List<Order> toEntitys(List<OrderDto> orderDtos);

    // Methods to convert OrderItem DTO onject into OrderItem model object and vice-versa
    @Mapping(target = "productId", source = "product.id")
    OrderItemDto toDto(OrderItem orderItem);
    @Mapping(target = "product.id", source = "productId")
    OrderItem toEntity(OrderItemDto orderItemDto);

    List<OrderItemDto> toOrderItemDtos(List<OrderItem> orderItems);
    List<OrderItem> toOrderItemEntities(List<OrderItemDto> orderItemDtos);
}
