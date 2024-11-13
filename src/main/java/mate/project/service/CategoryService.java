package mate.project.service;

import java.util.List;
import mate.project.dto.CategoryResponseDto;
import mate.project.dto.CreateCategoryRequestDto;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> findAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CreateCategoryRequestDto requestDto);

    CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDto);

    void deleteById(Long id);
}
