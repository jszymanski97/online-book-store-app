package mate.project.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.project.dto.CategoryResponseDto;
import mate.project.dto.CreateCategoryRequestDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.CategoryMapper;
import mate.project.model.Category;
import mate.project.repository.category.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private static final int DEFAULT_PAGE_SIZE = 5;

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponseDto> findAll(Pageable pageable) {
        Pageable effectivePageable = pageable != null ? pageable : PageRequest.of(0,
                DEFAULT_PAGE_SIZE);
        return categoryRepository.findAll(effectivePageable).stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryResponseDto getById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Category with id " + id + " not found"));
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryResponseDto save(CreateCategoryRequestDto requestDto) {
        if (requestDto == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Failed to create category");
        }
        Category model = categoryMapper.toEntity(requestDto);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public CategoryResponseDto update(Long id, CreateCategoryRequestDto requestDto) {
        Category model = categoryMapper.toEntity(requestDto);
        model.setId(id);
        return categoryMapper.toDto(categoryRepository.save(model));
    }

    @Override
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
        throw new EntityNotFoundException("Category with id " + id + " not found");
    }
}
