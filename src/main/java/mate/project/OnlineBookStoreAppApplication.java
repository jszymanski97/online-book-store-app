package mate.project;

import java.math.BigDecimal;
import java.util.List;
import mate.project.model.Book;
import mate.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreAppApplication {

    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreAppApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Book book = createBook();
                book.setTitle("Book Title");
                book.setAuthor("Author");
                book.setIsbn("isbn");
                book.setPrice(BigDecimal.valueOf(10));
                bookService.save(book);

                List<Book> allBooks = bookService.findAll();
                System.out.println(allBooks);
            }
        };
    }

    private Book createBook() {
        return new Book();
    }
}
