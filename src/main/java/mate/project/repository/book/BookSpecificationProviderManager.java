package mate.project.repository.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.exception.SpecificationNotFoundException;
import mate.project.model.Book;
import mate.project.repository.SpecificationProvider;
import mate.project.repository.SpecificationProviderManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {

    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new SpecificationNotFoundException(
                        "No specification provider found for key: " + key));
    }
}
