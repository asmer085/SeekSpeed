package com.example.users.mappers;

import com.example.users.dto.OrdersDTO;
import com.example.users.entity.Orders;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-28T14:59:36+0200",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.9 (Oracle Corporation)"
)
@Component
public class OrdersMapperImpl implements OrdersMapper {

    @Override
    public OrdersDTO ordersToOrdersDTO(Orders order) {
        if ( order == null ) {
            return null;
        }

        OrdersDTO ordersDTO = new OrdersDTO();

        ordersDTO.setEquipmentId( order.getEquipmentId() );
        ordersDTO.setUserId( order.getUserId() );

        return ordersDTO;
    }

    @Override
    public Orders ordersDTOToOrders(OrdersDTO ordersDTO) {
        if ( ordersDTO == null ) {
            return null;
        }

        Orders orders = new Orders();

        orders.setEquipmentId( ordersDTO.getEquipmentId() );
        orders.setUserId( ordersDTO.getUserId() );

        return orders;
    }
}
