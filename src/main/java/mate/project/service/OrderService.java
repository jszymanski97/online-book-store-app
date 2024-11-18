package mate.project.service;

import java.util.List;
import mate.project.dto.OrderItemResponseDto;
import mate.project.dto.OrderResponseDto;
import mate.project.dto.PlaceOrderDto;
import mate.project.dto.StatusDto;

public interface OrderService {
    OrderResponseDto placeOrder(PlaceOrderDto placeOrderDto);

    List<OrderResponseDto> findAll();

    void update(Long id, StatusDto statusDto);

    List<OrderItemResponseDto> findOrderItemsByOrderId(Long orderId);

    OrderItemResponseDto getOrderItemByIds(Long orderId, Long itemId);
}
