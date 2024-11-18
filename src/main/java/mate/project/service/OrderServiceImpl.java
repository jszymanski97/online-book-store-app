package mate.project.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.project.dto.OrderItemResponseDto;
import mate.project.dto.OrderResponseDto;
import mate.project.dto.PlaceOrderDto;
import mate.project.dto.StatusDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.OrderMapper;
import mate.project.model.Order;
import mate.project.model.OrderItem;
import mate.project.model.ShoppingCart;
import mate.project.model.User;
import mate.project.repository.orderrepository.OrderRepository;
import mate.project.repository.shoppingcart.ShoppingCartRepository;
import mate.project.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponseDto placeOrder(PlaceOrderDto placeOrderDto) {
        User user = getAuthenticatedUser();
        ShoppingCart usersShoppingCart = getUsersShoppingCart(user);
        BigDecimal total = usersShoppingCart.getCartItems().stream()
                .map(cartItem -> cartItem.getBook().getPrice().multiply(BigDecimal
                        .valueOf(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Order currentOrder = new Order();
        Set<OrderItem> orderItems = usersShoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setBook(cartItem.getBook());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setOrder(currentOrder);
                    orderItem.setPrice(cartItem.getBook().getPrice());
                    return orderItem;
                }).collect(Collectors.toSet());

        currentOrder.setUser(user);
        currentOrder.setTotal(total);
        currentOrder.setOrderDate(LocalDateTime.now());
        currentOrder.setShippingAddress(placeOrderDto.getShippingAddress());
        currentOrder.setOrderItems(orderItems);
        currentOrder.setStatus(Order.Status.COMPLETED);
        Order savedOrder = orderRepository.save(currentOrder);
        shoppingCartRepository.delete(usersShoppingCart);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> findAll() {
        User user = getAuthenticatedUser();
        return orderRepository.findAllByUserId(user.getId()).stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    public void update(Long id, StatusDto statusDto) {
        Order orderToUpdate = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find order with order id: " + id));
        try {
            Order.Status statusEnum = Order.Status.valueOf(statusDto.getStatus().toUpperCase());
            orderToUpdate.setStatus(statusEnum);
            orderRepository.save(orderToUpdate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + statusDto);
        }
    }

    @Override
    public List<OrderItemResponseDto> findOrderItemsByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId).getOrderItems().stream()
                .map(orderMapper::toOrderItemResponseDto)
                .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItemByIds(Long orderId, Long itemId) {

        return orderMapper.toOrderItemResponseDto(orderRepository.findOrderItem(orderId, itemId));
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username "
                        + username + " not found"));
    }

    private ShoppingCart getUsersShoppingCart(User user) {
        return shoppingCartRepository.findByUserWithCartItems(user)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + user.getEmail()));
    }
}
