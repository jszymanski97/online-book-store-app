package mate.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.CartItemRequestDto;
import mate.project.dto.CartItemUpdateDto;
import mate.project.dto.ShoppingCartResponseDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.ShoppingCartMapper;
import mate.project.model.Book;
import mate.project.model.CartItem;
import mate.project.model.ShoppingCart;
import mate.project.model.User;
import mate.project.repository.book.BookRepository;
import mate.project.repository.shoppingcart.ShoppingCartRepository;
import mate.project.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public List<ShoppingCartResponseDto> findAll() {
        User user = getAuthenticatedUser();
        return shoppingCartRepository.findAllByUserId(user.getId()).stream()
                .map(shoppingCartMapper::toDto)
                .toList();
    }

    @Override
    public ShoppingCartResponseDto addCartItem(CartItemRequestDto cartItemRequestDto) {
        User user = getAuthenticatedUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });
        Book book = bookRepository.findById(cartItemRequestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Book with id "
                        + cartItemRequestDto.getBookId() + " not found"));

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(cartItemRequestDto.getQuantity());
        cartItem.setBook(book);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toDto(shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto update(Long cartItemId, CartItemUpdateDto cartItemUpdateDto) {
        User user = getAuthenticatedUser();
        ShoppingCart usersShoppingCart = getUsersShoppingCart(user);

        CartItem cartItemToUpdate = usersShoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem with cartItemId "
                        + cartItemId + " not found"));
        cartItemToUpdate.setQuantity(cartItemUpdateDto.getQuantity());
        return shoppingCartMapper.toDto(shoppingCartRepository.save(usersShoppingCart));
    }

    @Override
    public void deleteCartItemById(Long cartItemId) {
        User user = getAuthenticatedUser();
        ShoppingCart usersShoppingCart = getUsersShoppingCart(user);
        CartItem cartItemToRemove = usersShoppingCart.getCartItems().stream()
                .filter(cartItem -> cartItem.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("CartItem with id "
                        + cartItemId + " not found"));
        usersShoppingCart.getCartItems().remove(cartItemToRemove);
        shoppingCartRepository.save(usersShoppingCart);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username "
                        + username + " not found"));
    }

    private ShoppingCart getUsersShoppingCart(User user) {
        return shoppingCartRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + user.getEmail()));

    }
}
