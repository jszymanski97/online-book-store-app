package mate.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.BookDto;
import mate.project.dto.BookDtoWithoutCategoryIds;
import mate.project.dto.BookSearchParameters;
import mate.project.dto.CreateBookRequestDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.BookMapper;
import mate.project.model.Book;
import mate.project.repository.book.BookRepository;
import mate.project.repository.book.BookSpecificationBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        if (createBookRequestDto == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create book");
        }
        Book model = bookMapper.toEntity(createBookRequestDto);
        return bookMapper.toBookDto(bookRepository.save(model));
    }

    @Override

    public List<BookDto> findAll(Pageable pageable) {
        Pageable effectivePageable = pageable != null ? pageable : PageRequest.of(0, 5);
        return bookRepository.findAll(effectivePageable).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    public List<BookDtoWithoutCategoryIds> findAllBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategoryId(id).stream()
                .map(bookMapper::toDtoWithoutCategoryIds)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + id + " not found"));
        return bookMapper.toBookDto(book);
    }

    @Override
    public BookDto updateBookById(Long id, CreateBookRequestDto bookDto) {
        Book modelBook = bookMapper.toEntity(bookDto);
        modelBook.setId(id);
        return bookMapper.toBookDto(bookRepository.save(modelBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookDto> search(BookSearchParameters searchParameters) {
        Specification<Book> bookSpecification = bookSpecificationBuilder.build(searchParameters);
        return bookRepository.findAll(bookSpecification).stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
