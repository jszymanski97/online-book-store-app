package mate.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;
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
        Book savedModel = bookRepository.save(model);
        return bookMapper.toBookDto(savedModel);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto getBookById(Long id) {
        Book bookById = bookRepository.getBookById(id);
        return bookMapper.toBookDto(bookById);
    }
}
