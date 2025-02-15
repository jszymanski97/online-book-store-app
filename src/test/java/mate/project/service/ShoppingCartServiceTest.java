package mate.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ShoppingCartServiceTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private ShoppingCartMapper shoppingCartMapper;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    private ShoppingCart shoppingCart;
    private ShoppingCartResponseDto shoppingCartResponseDto;
    private CartItemRequestDto cartItemRequestDto;
    private CartItemUpdateDto cartItemUpdateDto;
    private User user;
    private CartItem cartItem;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        book = new Book();
        book.setId(1L);

        cartItemRequestDto = new CartItemRequestDto();
        cartItemRequestDto.setBookId(book.getId());
        cartItemRequestDto.setQuantity(1);

        cartItemUpdateDto = new CartItemUpdateDto();
        cartItemUpdateDto.setQuantity(5);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setBook(book);
        cartItem.setQuantity(cartItemRequestDto.getQuantity());

        shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setUser(user);
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.getCartItems().add(cartItem);

        shoppingCartResponseDto = new ShoppingCartResponseDto();
        shoppingCartResponseDto.setId(shoppingCart.getId());
        shoppingCartResponseDto.setUserId(user.getId());

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Adding a new shopping cart item")
    void addCartItem_validCartItemRequestDto_updatedShoppingCart() {
        // Given
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(bookRepository.findById(cartItemRequestDto.getBookId())).thenReturn(Optional.of(book));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartResponseDto);
        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));

        // When
        ShoppingCartResponseDto actual = shoppingCartService.addCartItem(cartItemRequestDto);

        // Then
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    @DisplayName("Adding a shopping cart item with null bookId")
    void addCartItem_nullBookId_throwsUsernameNotFoundException() {
        // Given
        CartItemRequestDto invalidCartItemRequestDto = new CartItemRequestDto();
        invalidCartItemRequestDto.setBookId(null);
        invalidCartItemRequestDto.setQuantity(1);

        // When & Then
        assertThrows(UsernameNotFoundException.class,
                () -> shoppingCartService.addCartItem(invalidCartItemRequestDto));
        verify(bookRepository, times(0)).findById(any());
        verify(shoppingCartRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("Updating an existing shopping cart item")
    void updateCartItem_validCartItemUpdateDto_updatedShoppingCart() {
        //Given
        when(shoppingCartRepository.findByUser(user)).thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.save(shoppingCart)).thenReturn(shoppingCart);
        when(shoppingCartRepository.findCartItemsByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartResponseDto);

        //When
        ShoppingCartResponseDto actual = shoppingCartService
                .update(cartItem.getId(), cartItemUpdateDto);

        //Then
        assertEquals(cartItemUpdateDto.getQuantity(), cartItem.getQuantity());
        assertEquals(shoppingCartResponseDto, actual);
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    @DisplayName("Deleting a shopping cart item")
    void deleteCartItemById_validCartItemId_removesCartFromDb() {
        // Given
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.findCartItemsByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));

        // When
        shoppingCartService.deleteCartItemById(cartItem.getId());

        // Then
        assertEquals(0, shoppingCart.getCartItems().size());
        verify(shoppingCartRepository, times(1)).save(shoppingCart);
    }

    @Test
    @DisplayName("Deleting a non-existing shopping cart item")
    void deleteCartItemById_nonExistingCartItem_throwsEntityNotFoundException() {
        // Given
        when(shoppingCartRepository.findByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(shoppingCartRepository.findCartItemsByUser(user))
                .thenReturn(Optional.of(shoppingCart));
        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> shoppingCartService
                .deleteCartItemById(999L));
        verify(shoppingCartRepository, times(0)).save(shoppingCart);
    }

    @Test
    @DisplayName("Finding all shopping carts for authenticated user")
    void findAll_validUser_usersIdAndListOfShoppingCartItems() {
        // Given
        when(userRepository.findByEmail("testUser")).thenReturn(Optional.of(user));
        when(shoppingCartRepository.findAllByUserId(user.getId()))
                .thenReturn(List.of(shoppingCart));
        when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartResponseDto);

        // When
        List<ShoppingCartResponseDto> actual = shoppingCartService.findAll();

        // Then
        assertEquals(1, actual.size());
        assertEquals(shoppingCartResponseDto, actual.get(0));
        verify(shoppingCartRepository, times(1)).findAllByUserId(user.getId());
    }

    @Test
    @DisplayName("Finding all shopping carts for an invalid user")
    void findAll_invalidUser_throwsUsernameNotFoundException() {
        // Given
        when(userRepository.findByEmail("testUser"))
                .thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> shoppingCartService.findAll());
        verify(shoppingCartRepository, times(0)).findAllByUserId(anyLong());
    }
}
