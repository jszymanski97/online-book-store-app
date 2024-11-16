package mate.project.service;

import java.util.List;
import mate.project.dto.CartItemRequestDto;
import mate.project.dto.CartItemUpdateDto;
import mate.project.dto.ShoppingCartResponseDto;

public interface ShoppingCartService {
    List<ShoppingCartResponseDto> findAll();

    ShoppingCartResponseDto addCartItem(CartItemRequestDto cartItemRequestDto);

    ShoppingCartResponseDto update(Long cartItemId, CartItemUpdateDto cartItemRequestDto);

    void deleteCartItemById(Long cartItemId);
}
