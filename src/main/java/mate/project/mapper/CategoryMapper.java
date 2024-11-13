package mate.project.mapper;

import mate.project.config.MapperConfig;
import mate.project.dto.CategoryResponseDto;
import mate.project.dto.CreateCategoryRequestDto;
import mate.project.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Category toEntity(CreateCategoryRequestDto requestDto);
}
