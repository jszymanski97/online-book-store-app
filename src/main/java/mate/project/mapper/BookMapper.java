package mate.project.mapper;

import mate.project.config.MapperConfig;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;
import mate.project.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Book toModel(CreateBookRequestDto createBookRequestDto);
}
