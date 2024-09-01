package mate.project.repository;

import java.util.List;
import java.util.Optional;
import mate.project.model.Book;

public interface BookRepository {
    Book save(Book book);

    List<Book> findAll();

    Optional<Book> getBookById(Long id);
}
