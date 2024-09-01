package mate.project.mapper;

import mate.project.config.MapperConfig;
import mate.project.dto.BookDto;
import mate.project.dto.CreateBookRequestDto;
import mate.project.model.Book;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toBookDto(Book book);

    Book toModel(CreateBookRequestDto createBookRequestDto);
}
