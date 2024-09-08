package mate.project.repository.book.spec;

import java.util.Arrays;
import mate.project.model.Book;
import mate.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "isbn";
    }

    public Specification<Book> getSpecification(Object[] params) {
        return (root, query, criteriabuilder) -> root.get("isbn")
                .in(Arrays.stream(params).toArray());
    }
}
