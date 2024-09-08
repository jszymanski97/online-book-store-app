package mate.project.repository.book.spec;

import java.util.Arrays;
import mate.project.model.Book;
import mate.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    public Specification<Book> getSpecification(Object[] params) {
        return (root, query, criteriabuilder) -> root.get("author")
                .in(Arrays.stream(params).toArray());
    }
}

