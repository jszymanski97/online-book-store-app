package mate.project.service;

import java.util.List;
import mate.project.model.Book;

public interface BookService {
    Book save(Book book);

    List<Book> findAll();
}
