package com.luckyprogrammer.dnd_inventory_backend.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luckyprogrammer.dnd_inventory_backend.dtos.response.CategoryResponse;
import com.luckyprogrammer.dnd_inventory_backend.entities.Category;
import com.luckyprogrammer.dnd_inventory_backend.mappers.CategoryMapper;
import com.luckyprogrammer.dnd_inventory_backend.repositories.CategoryRepository;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Transactional(readOnly = true)
    public Optional<CategoryResponse> getCategoryById(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent())
            return Optional.of(categoryMapper.toResponse(categoryOptional.orElseThrow()));

        return Optional.empty();
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> searchAdminCategories(
            String name,
            Boolean enable,
            Long itemClassId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Category> categories = categoryRepository.searchCategories(
                name,
                enable,
                itemClassId,
                pageable);

        if (!categories.isEmpty()) {
            return categories.map(category -> categoryMapper.toResponse(category));
        }

        return Page.empty();
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> getCategoriesByItemClassId(
            Long itemClassId,
            int page,
            int size,
            String sortBy,
            String sortDir) {

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<Category> categories = categoryRepository.findByItemClassId(itemClassId, pageable);

        if (!categories.isEmpty()) {
            return categories.map(category -> categoryMapper.toResponse(category));
        }

        return Page.empty();
    }

    @Transactional
    public CategoryResponse createCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category con el nombre " + category.getName() + " esta en uso.");
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    @Transactional
    public Optional<CategoryResponse> updateCategory(Long id, Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category con el nombre " + category.getName() + " esta en uso.");
        }

        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent())
            return Optional.of(categoryMapper.toResponse(
                    categoryRepository.save(categoryOptional.orElseThrow())));

        return Optional.empty();
    }

    @Transactional
    public Optional<CategoryResponse> toggleCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            categoryOptional.get().setEnable(!categoryOptional.get().isEnable());
            return Optional.of(categoryMapper.toResponse(
                    categoryRepository.save(categoryOptional.orElseThrow())));
        }

        return Optional.empty();
    }

    @Transactional
    public Optional<CategoryResponse> deleteCategory(Long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isPresent()) {
            categoryRepository.delete(categoryOptional.orElseThrow());
            return Optional.of(categoryMapper.toResponse(categoryOptional.orElseThrow()));
        }

        return Optional.empty();
    }
}
