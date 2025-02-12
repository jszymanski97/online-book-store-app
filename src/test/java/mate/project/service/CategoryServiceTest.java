package mate.project.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.project.dto.CategoryResponseDto;
import mate.project.dto.CreateCategoryRequestDto;
import mate.project.exception.EntityNotFoundException;
import mate.project.mapper.CategoryMapper;
import mate.project.model.Category;
import mate.project.repository.category.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CreateCategoryRequestDto createCategoryRequestDto;
    private CategoryResponseDto categoryResponseDto;

    @BeforeEach
    void setUp() {
        category = new Category();
        category.setId(1L);
        category.setName("Test Category");
        category.setDescription("Test Category Description");

        createCategoryRequestDto = new CreateCategoryRequestDto(
                category.getName(),
                category.getDescription());

        categoryResponseDto = new CategoryResponseDto(
                category.getId(),
                category.getName(),
                category.getDescription());
    }

    @Test
    @DisplayName("Creating a new Category and saving it to database")
    void save_validCategoryDto_categoryResponseDto() {
        //Given
        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        //When
        CategoryResponseDto actual = categoryService.save(createCategoryRequestDto);

        //Then
        assertEquals(categoryResponseDto, actual);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Finding all categories from db")
    void findAll_withPagination_ListOfAllCategories() {
        // Given
        Pageable pageable = PageRequest.of(0, 5);
        Page<Category> mockedPage = new PageImpl<>(List.of(category), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(mockedPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        // When
        List<CategoryResponseDto> actual = categoryService.findAll(pageable);

        // Then
        List<CategoryResponseDto> expected = List.of(categoryResponseDto);
        assertEquals(expected, actual);
        verify(categoryRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Finding category from db by id")
    void getById_validId_ReturnsCategoryResponseDto() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        // When
        CategoryResponseDto actual = categoryService.getById(categoryId);

        // Then
        assertEquals(categoryResponseDto, actual);
        verify(categoryRepository, times(1)).findById(categoryId);
    }

    @Test
    @DisplayName("Updating category by id")
    void update_validIdAndCategory_ReturnsCategoryResponseDto() {
        // Given
        createCategoryRequestDto = new CreateCategoryRequestDto(
                category.getName(),
                "Updated Description");

        category.setDescription("Updated Description");
        categoryResponseDto = new CategoryResponseDto(
                category.getId(),
                category.getName(),
                "Updated Description");

        when(categoryMapper.toEntity(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryResponseDto);

        Long categoryId = 1L;
        // When
        CategoryResponseDto actual = categoryService.update(categoryId, createCategoryRequestDto);

        // Then
        assertEquals(categoryResponseDto, actual);
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    @DisplayName("Deleting a category by id")
    void deleteById_existingId_NoExceptionThrown() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(true);

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    @DisplayName("Deleting a category by id throws exception if not found")
    void deleteById_nonExistingId_ThrowsEntityNotFoundException() {
        // Given
        Long categoryId = 1L;
        when(categoryRepository.existsById(categoryId)).thenReturn(false);

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            categoryService.deleteById(categoryId);
        });

        // Then
        assertEquals("Category with id " + categoryId + " not found", exception.getMessage());
        verify(categoryRepository, times(0)).deleteById(categoryId);

    }
}
