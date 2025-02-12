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
import mate.project.dto.BookDtoWithoutCategoryIds;
import mate.project.dto.CategoryResponseDto;
import mate.project.dto.CreateCategoryRequestDto;
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
public class CategoryControllerTest {

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
    void createCategory_validCreateCategoryRequestDto_Success() throws Exception {
        //Given
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Extreme Horror",
                "Very scary book");
        CategoryResponseDto expected = new CategoryResponseDto(
                4L,
                requestDto.name(),
                requestDto.description());
        String jasonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                post("/categories")
                        .content(jasonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryResponseDto actual = objectMapper.readValue(result
                .getResponse()
                .getContentAsString(),
                CategoryResponseDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieves all categories from db")
    @Test
    void getAll_withPagination_returnPaginatedCategories() throws Exception {
        //Given
        int page = 0;
        int size = 2;
        String sort = "name,asc";
        List<CategoryResponseDto> expected = new ArrayList<>();
        expected.add(new CategoryResponseDto(
                1L, "Fantasy", "Fantasy books"));
        expected.add(new CategoryResponseDto(
                2L, "Fiction", "Fictional books"));

        //When
        MvcResult result = mockMvc.perform(
                get("/categories")
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryResponseDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), CategoryResponseDto[].class);
        Assertions.assertEquals(size, actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id"));
        }
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieves a category from db by ID")
    @Test
    void getCategoryById_validId_returnsCategory() throws Exception {
        //Given
        Long categoryId = 3L;
        CategoryResponseDto expected = new CategoryResponseDto(
                3L, "History","Historical books");

        //When
        MvcResult result = mockMvc.perform(
                get("/categories/{id}", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), CategoryResponseDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Updating an existing category")
    @Test
    void updateCategory_validUpdateCategoryRequestDto_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Updated Fiction",
                "Updated description for Fiction books");
        CategoryResponseDto expected = new CategoryResponseDto(
                categoryId,
                requestDto.name(),
                requestDto.description());
        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        // When
        MvcResult result = mockMvc.perform(
                        put("/categories/{id}", categoryId)
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        CategoryResponseDto actual = objectMapper.readValue(result
                        .getResponse()
                        .getContentAsString(),
                CategoryResponseDto.class);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Deleting a category by ID")
    @Test
    void deleteCategory_validId_noContent() throws Exception {
        // Given
        Long categoryId = 1L;

        // When & Then
        mockMvc.perform(delete("/categories/{id}", categoryId))
                .andExpect(status().isNoContent());
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieve a single book by category ID")
    @Test
    void getBooksByCategoryId_singleBook_Success() throws Exception {
        // Given
        Long categoryId = 1L;
        BookDtoWithoutCategoryIds expected = new BookDtoWithoutCategoryIds();
        expected.setId(1L);
        expected.setTitle("The Great Gatsby");
        expected.setAuthor("F. Scott Fitzgerald");
        expected.setIsbn("9781234567892");
        expected.setPrice(BigDecimal.valueOf(10.99));
        expected.setDescription("Classic American novel.");
        expected.setCoverImage("gatsby.jpg");
        // When
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertEquals(1, actual.length);
        Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected, actual[0], "id"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @DisplayName("Retrieve multiple books by category ID")
    @Test
    void getBooksByCategoryId_multipleBooks_Success() throws Exception {
        // Given

        BookDtoWithoutCategoryIds book1 = new BookDtoWithoutCategoryIds();
        book1.setId(1L);
        book1.setTitle("The Hobbit");
        book1.setAuthor("J.R.R. Tolkien");
        book1.setIsbn("9781234567890");
        book1.setPrice(BigDecimal.valueOf(10.99));
        book1.setDescription("A fantasy adventure.");
        book1.setCoverImage("hobbit.jpg");

        BookDtoWithoutCategoryIds book2 = new BookDtoWithoutCategoryIds();
        book2.setId(2L);
        book2.setTitle("The Great Gatsby");
        book2.setAuthor("F. Scott Fitzgerald");
        book2.setIsbn("9781234567892");
        book2.setPrice(BigDecimal.valueOf(10.99));
        book2.setDescription("Classic American novel.");
        book2.setCoverImage("gatsby.jpg");
        List<BookDtoWithoutCategoryIds> expected = List.of(book1, book2);

        Long categoryId = 2L;

        // When
        MvcResult result = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        BookDtoWithoutCategoryIds[] actual = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDtoWithoutCategoryIds[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        for (int i = 0; i < expected.size(); i++) {
            Assertions.assertTrue(EqualsBuilder.reflectionEquals(expected.get(i), actual[i], "id"));
        }
    }
}
