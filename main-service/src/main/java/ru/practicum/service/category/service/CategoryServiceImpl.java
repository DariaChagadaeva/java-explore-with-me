package ru.practicum.service.category.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.service.category.dto.CategoryDto;
import ru.practicum.service.category.dto.NewCategoryDto;
import ru.practicum.service.category.mapper.CategoryMapper;
import ru.practicum.service.category.model.Category;
import ru.practicum.service.category.repository.CategoryRepository;
import ru.practicum.service.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryServiceImpl implements CategoryService {
    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        log.info("Method addCategory invoke");
        CategoryDto category = categoryMapper.fromModelToDto(categoryRepository.save(categoryMapper.fromNewDtoToModel(newCategoryDto)));
        log.info("New category added {}", category);
        return category;
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        log.info("Method updateCategory invoke");
        Category updatedCategory = categoryRepository.findById(catId).orElseThrow(()
                -> new EntityNotFoundException("Category not found"));
        updatedCategory.setName(categoryDto.getName());
        return categoryMapper.fromModelToDto(categoryRepository.save(updatedCategory));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.info("Method deleteCategory invoke");
        categoryRepository.delete(categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException("Category not found")));
        log.info("Category {} deleted", catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        log.info("Method getAllCategories invoke");
        Pageable page = PageRequest.of(from / size, size);
        log.info("Get information about categories");
        return categoryRepository.findAll(page).getContent().stream()
                    .map(categoryMapper::fromModelToDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryDtoById(Long catId) {
        log.info("Get category {}", catId);
        return categoryMapper.fromModelToDto(categoryRepository.findById(catId).orElseThrow(()
                -> new EntityNotFoundException("Category not found")));
    }

    @Override
    public Category getCategoryById(Long catId) {
        log.info("Method getCategoryById invoke");
        return categoryRepository.findById(catId).orElseThrow(()
                -> new EntityNotFoundException("Category not found"));
    }
}
