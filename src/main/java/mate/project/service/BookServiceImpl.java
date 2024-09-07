package mate.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.BookMapper;
import mate.project.model.Book;
import mate.project.repository.BookRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookDto save(CreateBookRequestDto createBookRequestDto) {
        Book model = bookMapper.toModel(createBookRequestDto);
        return bookMapper.toBookDto(bookRepository.save(model));
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
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
        Book modelBook = bookMapper.toModel(bookDto);
        modelBook.setId(id);
        return bookMapper.toBookDto(bookRepository.save(modelBook));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
