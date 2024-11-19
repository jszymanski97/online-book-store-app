package mate.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.OrderItemResponseDto;
import mate.project.dto.OrderResponseDto;
import mate.project.dto.PlaceOrderDto;
import mate.project.dto.StatusDto;
import mate.project.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order Management", description = "Endpoints for managing orders and order items")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Place a new order", description =
            "Creates a new order with the provided details.")
    public OrderResponseDto placeOrder(@Valid @RequestBody PlaceOrderDto placeOrderDto) {
        return orderService.placeOrder(placeOrderDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all orders", description =
            "Retrieves a list of all orders for the authenticated user.")
    public List<OrderResponseDto> getAllOrders() {
        return orderService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    @Operation(summary = "Update order status", description =
            "Updates the status of an order by its ID.")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @Valid @RequestBody StatusDto status) {
        return orderService.update(id, status);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items")
    @Operation(summary = "Get order items", description =
            "Fetches all items for a specific order by order ID.")
    public List<OrderItemResponseDto> getOrderItems(@PathVariable Long orderId) {
        return orderService.findOrderItemsByOrderId(orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get specific order item", description =
            "Fetches a specific item from an order by order ID and item ID.")
    public OrderItemResponseDto getOrderItem(@PathVariable Long orderId,
                                             @PathVariable Long itemId) {
        return orderService.findOrderItemByOrderIdAndItemId(orderId, itemId);
    }
}
