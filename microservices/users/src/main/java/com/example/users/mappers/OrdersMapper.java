package com.example.users.mappers;

import com.example.users.dto.OrdersDTO;
import com.example.users.entity.Orders;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface OrdersMapper {
    OrdersMapper INSTANCE = Mappers.getMapper(OrdersMapper.class);

    OrdersDTO ordersToOrdersDTO(Orders order);
    Orders ordersDTOToOrders(OrdersDTO ordersDTO);
}
