package mate.project.repository.book.spec;

import java.math.BigDecimal;
import mate.project.model.Book;
import mate.project.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PriceSpecification implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "price";
    }

    public Specification<Book> getSpecification(Object[] priceRange) {
        BigDecimal minPrice = (BigDecimal) priceRange[0];
        BigDecimal maxPrice = (BigDecimal) priceRange[1];

        return (root, query, criteriaBuilder) -> {
            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
            return null;
        };
    }
}
