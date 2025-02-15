package mate.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import mate.project.model.ShoppingCart;
import mate.project.model.User;
import mate.project.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-shoppingCart.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by user - Existing user")
    void findByUser_existingUser_usersShoppingCart() {
        //Given
        User user = new User();
        user.setId(1L);

        //When
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user);

        //Then
        assertTrue(shoppingCart.isPresent());
        assertEquals(1L, shoppingCart.get().getId());
    }

    @Test
    @DisplayName("Find shopping cart by user - Non-existent user")
    void findByUser_nonExistingUser_returnsEmptyShoppingCart() {
        //Given
        User user = new User();
        user.setId(999L);

        //When
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findByUser(user);

        //Then
        assertTrue(shoppingCart.isEmpty());
    }

    @Test
    @DisplayName("Find all shopping carts by user ID - Existing user")
    void findAllByUserId_existingUser_listOfUsersShoppingCarts() {
        //Given
        Long userId = 1L;

        //When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userId);

        //Then
        assertEquals(1, shoppingCarts.size());
        assertEquals(1L, shoppingCarts.get(0).getId());
    }

    @Test
    @DisplayName("Find all shopping carts by user ID - Non-existent user")
    void findAllByUserId_nonExistingUser_returnsEmptyShoppingCart() {
        //Given
        Long userId = 999L;

        //When
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByUserId(userId);

        //Then
        assertTrue(shoppingCarts.isEmpty());
    }

    @Test
    @DisplayName("Find shopping cart with cart items by user - Existing user")
    void findCartItemsByUser_existingUser_returnsShoppingCartWithItems() {
        //Given
        User user = new User();
        user.setId(1L);

        //When
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findCartItemsByUser(user);

        //Then
        assertTrue(shoppingCart.isPresent());
        assertEquals(1L, shoppingCart.get().getId());
        assertFalse(shoppingCart.get().getCartItems().isEmpty());
    }

    @Test
    @DisplayName("Find shopping cart with cart items by user - Non-existent user")
    void findCartItemsByUser_nonExistingUser_returnsEmptyShoppingCart() {
        //Given
        User user = new User();
        user.setId(999L);

        //When
        Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findCartItemsByUser(user);

        //Then
        assertTrue(shoppingCart.isEmpty());
    }
}
