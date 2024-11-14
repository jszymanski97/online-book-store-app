package mate.project.service;

import java.util.List;
import mate.project.dto.BookDto;
import mate.project.dto.BookDtoWithoutCategoryIds;
import mate.project.dto.BookSearchParameters;
import mate.project.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> findAll(Pageable pageable);

    List<BookDtoWithoutCategoryIds> findAllBooksByCategoryId(Long categoryId);

    BookDto getBookById(Long id);

    BookDto updateBookById(Long id, CreateBookRequestDto bookDto);

    void deleteById(Long id);

    List<BookDto> search(BookSearchParameters searchParameters);
}
