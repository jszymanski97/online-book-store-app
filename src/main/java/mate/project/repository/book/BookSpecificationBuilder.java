package mate.project.repository.book;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import mate.project.dto.BookSearchParameters;
import mate.project.model.Book;
import mate.project.repository.SpecificationBuilder;
import mate.project.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {

    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParameters searchParameters) {
        Specification<Book> spec = Specification.where(null);
        Map<String, String[]> parametersMap = new HashMap<>();
        parametersMap.put("title", searchParameters.titles());
        parametersMap.put("author", searchParameters.authors());
        parametersMap.put("isbn", searchParameters.isbn());
        parametersMap.put("description", searchParameters.descriptionKeyword());

        for (Map.Entry<String, String[]> entry : parametersMap.entrySet()) {
            String[] paramValues = entry.getValue();
            if (paramValues != null && paramValues.length > 0) {
                spec = spec.and(bookSpecificationProviderManager
                        .getSpecificationProvider(entry.getKey())
                        .getSpecification(paramValues));
            }
        }
        if (searchParameters.minPrice() != null || searchParameters.maxPrice() != null) {
            BigDecimal minPrice = searchParameters.minPrice();
            BigDecimal maxPrice = searchParameters.maxPrice();
            spec = spec.and(bookSpecificationProviderManager
                    .getSpecificationProvider("price")
                    .getSpecification(new BigDecimal[]{minPrice, maxPrice}));
        }

        return spec;
    }
}
