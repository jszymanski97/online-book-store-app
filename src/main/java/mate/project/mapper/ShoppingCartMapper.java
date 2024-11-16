package mate.project.mapper;

import mate.project.config.MapperConfig;
import mate.project.dto.CartItemResponseDto;
import mate.project.dto.ShoppingCartResponseDto;
import mate.project.model.CartItem;
import mate.project.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface ShoppingCartMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "cartItems", target = "cartItems")
    ShoppingCartResponseDto toDto(ShoppingCart shoppingCart);

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemResponseDto toCartItemDto(CartItem cartItem);
}
