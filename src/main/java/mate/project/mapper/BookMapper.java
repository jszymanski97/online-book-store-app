package mate.project.mapper;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import mate.project.config.MapperConfig;
import mate.project.dto.BookDto;
import mate.project.dto.BookDtoWithoutCategoryIds;
import mate.project.dto.CreateBookRequestDto;
import mate.project.model.Book;
import mate.project.model.Category;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoriesIds", ignore = true)
    BookDto toBookDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "categories", source = "categories",
            qualifiedByName = "mapCategoryIdsToCategories")
    Book toEntity(CreateBookRequestDto createBookRequestDto);

    BookDtoWithoutCategoryIds toDtoWithoutCategoryIds(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        if (book.getCategories() != null) {
            bookDto.setCategoriesIds(
                    book.getCategories().stream()
                            .map(Category::getId)
                            .collect(Collectors.toSet())
            );
        }
    }

    @org.mapstruct.Named("mapCategoryIdsToCategories")
    default Set<Category> mapCategoryIdsToCategories(Set<Long> categoryIds) {
        if (categoryIds == null) {
            return new HashSet<>();
        }
        return categoryIds.stream()
                .map(id -> {
                    Category category = new Category();
                    category.setId(id);
                    return category;
                })
                .collect(Collectors.toSet());
    }
}
