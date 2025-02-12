package mate.project.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookControllerTest {

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

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Adding a book to db")
    @Test
    void createBook_ValidCreateBookRequestDto_Success() throws Exception {
        //given

        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "Tom and Jerry",
                "Unknown",
                "ijo12421h32489f",
                BigDecimal.valueOf(43),
                "An interesting book",
                "tomAndJerry.jpg",
                Set.of(1L));
        BookDto expected = new BookDto()
                .setTitle(requestDto.title())
                .setAuthor(requestDto.author())
                .setIsbn(requestDto.isbn())
                .setDescription(requestDto.description())
                .setPrice(requestDto.price())
                .setCoverImage(requestDto.coverImage())
                .setCategoriesIds(Set.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto actual = objectMapper.readValue(result
                .getResponse().getContentAsString(),
                BookDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieves all books from db")
    @Test
    void getAll_withPagination_ReturnPaginatedBooks() throws Exception {
        //Given
        int page = 0;
        int size = 2;
        String sort = "title,asc";
        List<BookDto> expected = new ArrayList<>();

        expected.add(new BookDto()
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9781234567892")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("Classic American novel.")
                .setCoverImage("gatsby.jpg")
                .setCategoriesIds(Set.of(1L,2L))
        );
        expected.add(new BookDto()
                .setTitle("The Hobbit")
                .setAuthor("J.R.R. Tolkien")
                .setIsbn("9781234567890")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("A fantasy adventure.")
                .setCoverImage("hobbit.jpg")
                .setCategoriesIds(Set.of(2L))
        );

        //When
        MvcResult result = mockMvc.perform(
                get("/books")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(size, actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieves a book by ID")
    @Test
    void getBookById_ValidId_ReturnsBook() throws Exception {
        //Given
        Long bookId = 3L;
        BookDto expected = new BookDto()
                .setTitle("The Great Gatsby")
                .setAuthor("F. Scott Fitzgerald")
                .setIsbn("9781234567892")
                .setPrice(BigDecimal.valueOf(10.99))
                .setDescription("Classic American novel.")
                .setCoverImage("gatsby.jpg")
                .setCategoriesIds(Set.of(1L, 2L));
        // When
        MvcResult result = mockMvc.perform(
                        get("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Updates a book by ID")
    @Test
    void updateBookById_ValidIdAndData_ReturnsUpdatedBook() throws Exception {
        Long bookId = 3L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto(
                "The Great Gatsby - Updated",
                "F. Scott Fitzgerald",
                "9781234567892",
                BigDecimal.valueOf(12.99),
                "Updated description.",
                "gatsby_updated.jpg",
                Set.of(1L, 2L)
        );

        BookDto expected = new BookDto()
                .setTitle(requestDto.title())
                .setAuthor(requestDto.author())
                .setIsbn(requestDto.isbn())
                .setPrice(requestDto.price())
                .setDescription(requestDto.description())
                .setCoverImage(requestDto.coverImage())
                .setCategoriesIds(requestDto.categories());

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        put("/books/{id}", bookId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Deletes a book by ID")
    @Test
    void deleteBookById_ValidId_NoContent() throws Exception {
        // Given
        Long bookId = 1L;

        // When & Then
        mockMvc.perform(
                        delete("/books/{id}", bookId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Searches for books by title - War and Peace")
    @Test
    void searchBooks_ByTitle_ReturnsMatchingBooks() throws Exception {
        // Given
        List<BookDto> expected = List.of(
                new BookDto()
                        .setTitle("War and Peace")
                        .setAuthor("Leo Tolstoy")
                        .setIsbn("9781234567891")
                        .setPrice(BigDecimal.valueOf(20.99))
                        .setDescription("Historical epic.")
                        .setCoverImage("war_peace.jpg")
                        .setCategoriesIds(Set.of(3L))
        );
        // When
        MvcResult result = mockMvc.perform(get("/books/search?titles=War and Peace")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Searches for books by max price of 11.00")
    @Test
    void searchBooks_ByPrice_ReturnsMatchingBooks() throws Exception {
        // Given
        List<BookDto> expected = List.of(
                new BookDto()
                        .setTitle("The Hobbit")
                        .setAuthor("J.R.R. Tolkien")
                        .setIsbn("9781234567890")
                        .setPrice(BigDecimal.valueOf(10.99))
                        .setDescription("A fantasy adventure.")
                        .setCoverImage("hobbit.jpg")
                        .setCategoriesIds(Set.of(2L)),
                new BookDto()
                        .setTitle("The Great Gatsby")
                        .setAuthor("F. Scott Fitzgerald")
                        .setIsbn("9781234567892")
                        .setPrice(BigDecimal.valueOf(10.99))
                        .setDescription("Classic American novel.")
                        .setCoverImage("gatsby.jpg")
                        .setCategoriesIds(Set.of(1L, 2L))
        );

        // When
        MvcResult result = mockMvc.perform(get("/books/search?maxPrice=11.00")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);

        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Searches for a nonexistent book, expecting an empty result")
    @Test
    void searchBooks_ByNonExistentTitle_ReturnsEmptyResult() throws Exception {
        //Given
        String nonExistentTitle = "Nonexistent Book";

        //When
        MvcResult result = mockMvc.perform(get("/books/search")
                        .param("titles", nonExistentTitle)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        //Then
        BookDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class);
        Assertions.assertEquals(0, actual.length,
                "Expected no books to be returned, but some were.");
    }
}
