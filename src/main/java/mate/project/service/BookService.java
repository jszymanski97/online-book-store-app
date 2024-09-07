package mate.project.service;

import java.util.List;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll();

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);
}
