package mate.project.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import mate.project.dto.CartItemRequestDto;
import mate.project.dto.CartItemResponseDto;
import mate.project.dto.CartItemUpdateDto;
import mate.project.dto.ShoppingCartResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-shoppingCart.sql",
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ShoppingCartControllerTest {
    private static final String EXAMPLE_USER = "user1@example.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = EXAMPLE_USER, roles = {"USER"})
    @DisplayName("Fetch shopping cart for user1")
    @Test
    void getShoppingCart_forUser1_returnsCorrectCartItems() throws Exception {
        //When
        MvcResult result = mockMvc.perform(get("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        String content = result.getResponse().getContentAsString();
        List<ShoppingCartResponseDto> cartItems = objectMapper
                .readValue(content,
                        new TypeReference<List<ShoppingCartResponseDto>>() {});

        Assertions.assertNotNull(cartItems);
        Assertions.assertEquals(1, cartItems.size());
        ShoppingCartResponseDto cart = cartItems.get(0);
        Assertions.assertEquals(1, cart.getUserId());

        List<CartItemResponseDto> sortedCartItems = new ArrayList<>(cart.getCartItems());
        sortedCartItems.sort(Comparator.comparing(CartItemResponseDto::getBookId));
        // Converted set to list to allow sorting and checking the books in cart

        CartItemResponseDto firstItem = sortedCartItems.get(0);
        Assertions.assertEquals("The Hobbit", firstItem.getBookTitle());
        Assertions.assertEquals(2, firstItem.getQuantity());
    }

    @WithMockUser(username = EXAMPLE_USER, roles = {"USER"})
    @DisplayName("Fetch shopping cart for user1")
    @Test
    void addBookToCart_forUser1_returnsShoppingCart() throws Exception {
        //Given
        CartItemRequestDto newCartItem = new CartItemRequestDto();
        newCartItem.setBookId(2L);
        newCartItem.setQuantity(2);

        String requestContent = objectMapper.writeValueAsString(newCartItem);

        //When
        MvcResult result = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String content = result.getResponse().getContentAsString();
        ShoppingCartResponseDto shoppingCart = objectMapper.readValue(
                content, ShoppingCartResponseDto.class);

        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(3, shoppingCart.getCartItems().size());
    }

    @WithMockUser(username = EXAMPLE_USER, roles = {"USER"})
    @DisplayName("Update cart item for user1")
    @Test
    void updateCartItem_forUser1_returnsUpdatedShoppingCart() throws Exception {
        // Given
        Long cartItemId = 1L;
        CartItemUpdateDto updateDto = new CartItemUpdateDto();
        updateDto.setQuantity(3);

        String requestContent = objectMapper.writeValueAsString(updateDto);

        // When
        MvcResult result = mockMvc.perform(put("/cart/cart-items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestContent))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String content = result.getResponse().getContentAsString();
        ShoppingCartResponseDto shoppingCart = objectMapper.readValue(
                content, ShoppingCartResponseDto.class);

        Assertions.assertNotNull(shoppingCart);
        Assertions.assertEquals(2, shoppingCart.getCartItems().size());

        CartItemResponseDto updatedItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow();
        Assertions.assertEquals(3, updatedItem.getQuantity());
    }

    @WithMockUser(username = EXAMPLE_USER, roles = {"USER"})
    @DisplayName("Delete cart item for user1")
    @Test
    void deleteCartItem_forUser1_returnsNoContent() throws Exception {
        // Given
        Long cartItemId = 1L;

        // When
        MvcResult result = mockMvc.perform(delete("/cart/cart-items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        //Then
        Assertions.assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }
}
