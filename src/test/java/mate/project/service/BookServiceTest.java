package mate.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.project.dto.BookDto;
import mate.project.dto.BookDtoWithoutCategoryIds;
import mate.project.dto.BookSearchParameters;
import mate.project.dto.CreateBookRequestDto;
import mate.project.mapper.BookMapper;
import mate.project.model.Book;
import mate.project.model.Category;
import mate.project.repository.book.BookRepository;
import mate.project.repository.book.BookSpecificationBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book book;
    private CreateBookRequestDto requestDto;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setIsbn("1234567890");
        book.setDescription("Test Book Description");
        book.setPrice(BigDecimal.valueOf(75.30));
        book.setCoverImage("TestCoverImage.jpg");
        Category category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Category Description");
        book.setCategories(Set.of(category));

        requestDto = new CreateBookRequestDto(
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPrice(),
                book.getDescription(),
                book.getCoverImage(),
                Set.of(1L));

        bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setAuthor(book.getAuthor());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setDescription(book.getDescription());
        bookDto.setPrice(book.getPrice());
        bookDto.setCoverImage(book.getCoverImage());
        bookDto.setCategoriesIds(Set.of(category.getId()));
    }

    @Test
    @DisplayName("Creating a new Book and saving it to database")
    void save_validCreateBookRequestDto_ReturnsBookDto() {
        //Given
        when(bookMapper.toEntity(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        //When
        BookDto actual = bookService.save(requestDto);
        //Then
        BookDto expected = bookDto;
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Finding all books from db")
    void findAll_withPagination_ListOfAllBooks() {
        //Given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> mockedPage = new PageImpl<>(List.of(book), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(mockedPage);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        //When
        List<BookDto> actual = bookService.findAll(pageable);
        //Then
        List<BookDto> expected = List.of(bookDto);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Finding all books from db by category id")
    void findAllBooksByCategoryId_validCategoryId_ListOfBookDtoWithoutCategoryIds() {
        //Given
        BookDtoWithoutCategoryIds bookDtoWithoutCategoryIds = new BookDtoWithoutCategoryIds();
        bookDtoWithoutCategoryIds.setId(book.getId());
        bookDtoWithoutCategoryIds.setTitle(book.getTitle());
        bookDtoWithoutCategoryIds.setAuthor(book.getAuthor());
        bookDtoWithoutCategoryIds.setIsbn(book.getIsbn());
        bookDtoWithoutCategoryIds.setDescription(book.getDescription());
        bookDtoWithoutCategoryIds.setPrice(book.getPrice());
        bookDtoWithoutCategoryIds.setCoverImage(book.getCoverImage());

        Long categoryId = 1L;
        when(bookRepository.findAllByCategoryId(categoryId)).thenReturn(List.of(book));
        when(bookMapper.toDtoWithoutCategoryIds(book)).thenReturn(bookDtoWithoutCategoryIds);
        //When
        List<BookDtoWithoutCategoryIds> actual = bookService.findAllBooksByCategoryId(categoryId);
        //Then
        List<BookDtoWithoutCategoryIds> expected = List.of(bookDtoWithoutCategoryIds);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findAllByCategoryId(categoryId);
    }

    @Test
    @DisplayName("Finding book from db by id")
    void getBookById_validId_ReturnsBookDto() {
        //Given
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.ofNullable(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        //When
        BookDto actual = bookService.getBookById(bookId);
        //Then
        BookDto expected = bookDto;
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    @DisplayName("Finding book from db by id")
    void updateBookById_validIdAndBook_ReturnsBookDto() {
        //Given
        CreateBookRequestDto updateRequestDto = new CreateBookRequestDto(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn(),
                        book.getPrice(),
                        "Updated description",
                        book.getCoverImage(),
                        Set.of(1L));
        book.setDescription("Updated description");
        bookDto.setDescription("Updated description");
        when(bookMapper.toEntity(updateRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);
        //When
        Long bookId = 1L;
        BookDto actual = bookService.updateBookById(bookId, updateRequestDto);
        //Then
        BookDto expected = bookDto;
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    @DisplayName("Deleting a book by id")
    void deleteById_existingId_noExceptionThrown() {
        // Given
        Long bookId = 1L;

        // When
        bookService.deleteById(bookId);

        // Then
        verify(bookRepository, times(1)).deleteById(bookId);
    }

    @Test
    @DisplayName("Searching books by search parameters returns a list of BookDtos")
    void search_validSearchParameters_ReturnsListOfBookDtos() {
        // Given
        BookSearchParameters searchParameters = new BookSearchParameters(
                new String[]{"Book Title"}, null,
                null, null, null, null);
        Specification<Book> bookSpecification = mock(Specification.class);
        when(bookSpecificationBuilder.build(searchParameters)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification)).thenReturn(List.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(bookDto);

        // When
        List<BookDto> actual = bookService.search(searchParameters);

        // Then
        List<BookDto> expected = List.of(bookDto);
        assertEquals(expected, actual);
        verify(bookSpecificationBuilder, times(1)).build(searchParameters);
        verify(bookRepository, times(1)).findAll(bookSpecification);
        verify(bookMapper, times(1)).toBookDto(book);
    }
}
