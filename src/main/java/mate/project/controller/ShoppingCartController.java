package mate.project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.CartItemRequestDto;
import mate.project.dto.CartItemUpdateDto;
import mate.project.dto.ShoppingCartResponseDto;
import mate.project.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart Management", description = "Endpoints for managing shopping cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get all from shopping cart", description = "Fetches for users shopping "
            + "cart and its items available in the database")
    public List<ShoppingCartResponseDto> getAll() {
        return shoppingCartService.findAll();
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @Operation(summary = "Add item to shopping cart", description = "Adding cart items to "
            + "users shopping cart")
    public ShoppingCartResponseDto addBookToCart(
            @Valid @RequestBody CartItemRequestDto cartItemRequestDto) {
        return shoppingCartService.addCartItem(cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Update item in shopping cart", description = "Updates item in "
            + "users shopping cart by items cart id")
    public ShoppingCartResponseDto updateCartItem(@Valid @PathVariable Long cartItemId,
            @Valid @RequestBody CartItemUpdateDto cartItemRequestDto) {
        return shoppingCartService.update(cartItemId, cartItemRequestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    @Operation(summary = "Delete cart item by id", description = "Removes a cart item from"
            + " users shopping cart using its unique id")

    public void deleteCartItemById(@PathVariable Long cartItemId) {
        shoppingCartService.deleteCartItemById(cartItemId);
    }
}
