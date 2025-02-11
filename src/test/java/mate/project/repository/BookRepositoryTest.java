package mate.project.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mate.project.model.Book;
import mate.project.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Sql(scripts = "/db/add-test-books.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/db/clean-data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category ID")
    void findAllByCategoryId_CategoryExists_ReturnsBooks() {
        Long categoryId = 2L;

        List<Book> books = bookRepository.findAllByCategoryId(categoryId);

        assertEquals(2, books.size(), "Expected 2 books for the category");
        assertEquals("The Hobbit", books.get(0).getTitle(), "First book title should match");
    }

    @Test
    @DisplayName("Find all books by category ID - No books")
    void findAllByCategoryId_CategoryDoesNotExist_ReturnsEmptyList() {
        Long categoryId = 999L;

        List<Book> books = bookRepository.findAllByCategoryId(categoryId);

        assertTrue(books.isEmpty(), "Expected no books for the non-existent category");
    }

    @Test
    @DisplayName("Soft delete - Find books after deletion")
    void softDelete_BookIsDeleted_NotReturnedInQuery() {
        Long bookIdToDelete = 1L;

        bookRepository.deleteById(bookIdToDelete);

        List<Book> books = bookRepository.findAll();

        assertFalse(books.stream().anyMatch(book -> book.getId().equals(bookIdToDelete)),
                "Deleted book should not appear in the result");
    }
}
