package mate.project.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import mate.project.config.MapperConfig;
import mate.project.dto.OrderItemResponseDto;
import mate.project.dto.OrderResponseDto;
import mate.project.model.Order;
import mate.project.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems", qualifiedByName =
            "toOrderItemResponseDtoSet")
    OrderResponseDto toDto(Order order);

    @Mapping(source = "book.id", target = "bookId")
    OrderItemResponseDto toOrderItemResponseDto(OrderItem orderItem);

    @Named("toOrderItemResponseDtoSet")
    default Set<OrderItemResponseDto> toOrderItemResponseDtoSet(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::toOrderItemResponseDto)
                .collect(Collectors.toSet());
    }
}
